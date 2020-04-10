package ro.atlas.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.atlas.commands.AtlasCommand;
import ro.atlas.commands.AtlasCommandType;
import ro.atlas.dto.AtlasGatewayAddDto;
import ro.atlas.dto.AtlasUsernamePassDto;
import ro.atlas.entity.AtlasClient;
import ro.atlas.entity.AtlasGateway;
import ro.atlas.entity.sample.AtlasDataSample;
import ro.atlas.exception.ClientNotFoundException;
import ro.atlas.exception.GatewayDuplicateKeyException;
import ro.atlas.exception.GatewayNotFoundException;
import ro.atlas.exception.GatewayNotRegisteredException;
import ro.atlas.properties.AtlasProperties;
import ro.atlas.repository.AtlasGatewayRepository;
import ro.atlas.service.AtlasGatewayService;

@Component
public class AtlasGatewayServiceImpl implements AtlasGatewayService {

    private static final Logger LOG = LoggerFactory.getLogger(AtlasGatewayServiceImpl.class);

    /* Publish-subscribe topic suffix which allows full-duplex communication with the gateway */
    private static final String ATLAS_TO_GATEWAY_TOPIC = "-to-gateway";
    private static final String ATLAS_TO_CLOUD_TOPIC = "-to-cloud";

    private @Autowired
    AtlasGatewayRepository gatewayRepository;
    private @Autowired
    AtlasMqttServiceImpl mqttService;
    private @Autowired
    AtlasProperties properties;

    @Transactional
    @Override
    public synchronized void addGateway(AtlasGatewayAddDto gatewayAddDto) {
        LOG.info("Adding gateway info...");

        /* Create initial gateway state */
        AtlasGateway gateway = new AtlasGateway();
        gateway.setAlias(gatewayAddDto.getAlias());
        gateway.setIdentity(gatewayAddDto.getIdentity());
        gateway.setPsk(gatewayAddDto.getPsk());
        gateway.setClients(new HashMap<>());

        try {
            /* Add gateway to database */
            gateway = gatewayRepository.save(gateway);
            /* Init communication with gateway */
            initGateway(gateway);
        } catch (DuplicateKeyException e) {
            LOG.error(e.getMessage());
            throw new GatewayDuplicateKeyException(Objects.requireNonNull(e.getMessage()));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public synchronized void messageReceived(String topic, byte[] payload) {
        AtlasGateway gateway = null;

        if (payload == null || payload.length == 0)
            return;

        /* Remove trailing full-duplex communication suffix */
        if (!topic.endsWith(ATLAS_TO_CLOUD_TOPIC)) {
            LOG.error("Message received on invalid topic: " + topic);
            return;
        }

        String psk = topic.substring(0, topic.lastIndexOf(ATLAS_TO_CLOUD_TOPIC));

        try {
            gateway = gatewayRepository.findByPsk(psk);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        if (gateway == null) {
            LOG.info("Message received from unknown gateway");
            return;
        }

        LOG.info("Message received for gateway with identity: " + gateway.getIdentity());

        /* Parse message */
        try {
            JSONObject jsonObject = new JSONObject(new String(payload));
            String cmdType = jsonObject.getString(AtlasCommandType.ATLAS_CMD_TYPE_FIELDNAME);

            /* Check command type */
            if (cmdType.equalsIgnoreCase(AtlasCommandType.ATLAS_CMD_GATEWAY_CLIENT_INFO_UPDATE.getCommandType())) {
                LOG.info("Gateway with identity " + gateway.getIdentity() + " sent a device update command");
                String cmdPayload = jsonObject.getString(AtlasCommandType.ATLAS_CMD_PAYLOAD_FIELDNAME);
                updateCommand(gateway, cmdPayload);
            } else if (cmdType.equalsIgnoreCase(AtlasCommandType.ATLAS_CMD_GATEWAY_REGISTER.getCommandType())) {
                LOG.info("Gateway with identity " + gateway.getIdentity() + " sent a register command");
                registerNow(gateway);
            } else if (cmdType.equalsIgnoreCase(AtlasCommandType.ATLAS_CMD_GATEWAY_KEEPALIVE.getCommandType())) {
                LOG.info("Gateway with identity " + gateway.getIdentity() + " sent a keep-alive command");
                keepaliveNow(gateway);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public List<AtlasGateway> getAllGateways() {
        List<AtlasGateway> gateways = null;
        try {
            gateways = gatewayRepository.findAllExcludeClients();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        return gateways;
    }

    @Override
    public AtlasGateway getGateway(String gw_identity) {
        AtlasGateway gateway = null;
        try {
            gateway = gatewayRepository.findByIdentity(gw_identity);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        if (gateway == null) {
            LOG.debug("Gateway with identity " + gw_identity + " not found!");
            throw new GatewayNotFoundException(gw_identity);
        }

        return gateway;
    }

    @Override
    public List<AtlasClient> getAllClients(String identity) {
        HashMap<String, AtlasClient> clients = null;
        try {
            clients = getGateway(identity).getClients();
        } catch (GatewayNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        if (clients == null)
            return null;

        return new ArrayList<AtlasClient>(clients.values());
    }

    @Override
    public AtlasClient getClient(String gw_identity, String cl_identity) {
        AtlasGateway gateway = getGateway(gw_identity);

        AtlasClient client = gateway.getClients().get(cl_identity);
        if (client == null) {
            LOG.debug("There are no client with identity " + cl_identity + " within gateway with identity " + gw_identity);
            throw new ClientNotFoundException(cl_identity);
        }

        return client;
    }

    @Override
    public void deleteGateway(AtlasGateway gw) {
        gatewayRepository.delete(gw);
    }

    @Override
    public void deleteClient(AtlasGateway gateway, String cl_identity) {
        gateway.getClients().remove(cl_identity);
        gatewayRepository.save(gateway);
    }

    @Transactional
    private void updateCommand(AtlasGateway gateway, String cmdPayload) {
        LOG.info("Update device for gateway with identity " + gateway.getIdentity());

        ObjectMapper mapper = new ObjectMapper();
        AtlasClient clientInfo = null;

        try {
            clientInfo = mapper.readValue(cmdPayload.getBytes(), AtlasClient.class);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        AtlasClient client = gateway.getClients().get(clientInfo.getIdentity());
        if (client == null) {
            /* If the client is new, set empty history queues */
            clientInfo.initHistorySamples();

            gateway.getClients().put(clientInfo.getIdentity(), clientInfo);
        } else
            client.updateInfo(clientInfo);

        gatewayRepository.save(gateway);
    }

    @Transactional
    private void registerNow(AtlasGateway gateway) {
        LOG.info("Set register state for gateway with identity " + gateway.getIdentity());

        gateway.setRegistered(true);
        gateway.setKeepaliveCounter(properties.getKeepaliveCounter());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		gateway.setLastRegistertTime(dateFormat.format(new Date()));
		/*
		 * If the gateway registers now, then mark all clients as offline. The gateway
		 * should send a full device update.
		 */
        gateway.getClients().forEach((identity, client) -> client.setRegistered("false"));
        
        gateway = gatewayRepository.save(gateway);
        
        /* When gateway is registered also simulate a keep-alive command */
        keepaliveNow(gateway);
    }

    @Transactional
    private void keepaliveNow(AtlasGateway gateway) {
        LOG.info("Set keep-alive state for gateway with identity " + gateway.getIdentity());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        gateway.setLastKeepaliveTime(dateFormat.format(new Date()));
        gateway.setKeepaliveCounter(properties.getKeepaliveCounter());

        gatewayRepository.save(gateway);
    }

    @Transactional
    private void decrementKeepalive(AtlasGateway gateway) {
        LOG.info("Decrement keep-alive counter for gateway with identity " + gateway.getIdentity());

        if (gateway.getKeepaliveCounter() == 0) {
            LOG.info("Gateway with identity " + gateway.getIdentity() + " becomes inactive");
            gateway.setRegistered(false);

            LOG.info("All the clients of the gateway with identity " + gateway.getIdentity() + " become inactive");
            for (Map.Entry<String, AtlasClient> client : gateway.getClients().entrySet())
                client.getValue().setRegistered("false"); //why is String??
        } else
            gateway.setKeepaliveCounter(gateway.getKeepaliveCounter() - 1);

        gatewayRepository.save(gateway);
    }

    @Override
    public synchronized void keepaliveTask() {
        LOG.info("Run periodic keep-alive task to detect inactive gateways");

        boolean shouldSubscribe = mqttService.shouldSubscribeAllTopics();
        LOG.info("Subscribe to all topics again: " + shouldSubscribe);
        
        List<AtlasGateway> gateways = gatewayRepository.findAll();
        gateways.forEach((gateway) -> {
            /* Subscribe to MQTT topic again, if necessary */
            if (shouldSubscribe) {
				LOG.info("Subscribe again to topic " + gateway.getPsk() + ATLAS_TO_CLOUD_TOPIC
						+ " for gateway with identity " + gateway.getIdentity() + " (" + gateway.getAlias() + ")");
				initGateway(gateway);
            } else
            	decrementKeepalive(gateway);
        });
    }

    @Transactional
    private void initGateway(AtlasGateway gateway) {
        LOG.info("Init gateway with identity " + gateway.getIdentity());

        /* Subscribe to the gateway topic (PSK) */
        mqttService.addSubscribeTopic(gateway.getPsk() + ATLAS_TO_CLOUD_TOPIC);

        /* Mark the gateway and the clients as offline */
        gateway.setRegistered(false);
        gateway.getClients().forEach((identity, client) -> client.setRegistered("false"));

        gateway = gatewayRepository.save(gateway);

        /* Request a registration from gateway */
        AtlasCommand cmd = new AtlasCommand();
        cmd.setCommandType(AtlasCommandType.ATLAS_CMD_GATEWAY_REGISTER_REQUEST);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonCmd = mapper.writeValueAsString(cmd);
            mqttService.publish(gateway.getPsk() + ATLAS_TO_GATEWAY_TOPIC, jsonCmd);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        /* Request a full device sync from the gateway */
        try {
            reqFullDeviceSync(gateway);
        } catch (GatewayNotRegisteredException e) {
            LOG.debug(e.getMessage());
        }
    }

    @Override
    public synchronized void initGateways() {
        LOG.info("Init gateways at application start-up");

        /* Allow gateway to connect to the cloud broker */
        syncPermittedMqttGateways();
        
        List<AtlasGateway> gateways = gatewayRepository.findAll();
        gateways.forEach((gateway) -> {
            initGateway(gateway);
        });
    }

    @Override
    public void reqFullDeviceSync(AtlasGateway gateway) {
        if (!gateway.isRegistered()) {
            LOG.debug("Gateway with identity " + gateway.getIdentity() + " is not online");
            throw new GatewayNotRegisteredException(gateway.getIdentity());
        }

        AtlasCommand cmd = new AtlasCommand();
        cmd.setCommandType(AtlasCommandType.ATLAS_CMD_GATEWAY_GET_ALL_DEVICES);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonCmd = mapper.writeValueAsString(cmd);
            mqttService.publish(gateway.getPsk() + ATLAS_TO_GATEWAY_TOPIC, jsonCmd);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void syncPermittedMqttGateways() {
    	List<AtlasUsernamePassDto> usernamePass = new ArrayList<>();
    	
    	LOG.info("Set the list of gateways which are allowed to connect to the cloud broker");
    	
        List<AtlasGateway> gateways = gatewayRepository.findAll();
        for (AtlasGateway gateway : gateways) {
			LOG.info("Allow gateway " + gateway.getAlias() + " with identity " + gateway.getIdentity()
					+ " to connect to cloud broker");
        	usernamePass.add(new AtlasUsernamePassDto(gateway.getIdentity(), gateway.getPsk()));
        }
        
        /* Sync MQTT credentials */
        mqttService.syncUsernamePass(usernamePass);
    }

    private void updateClientsReputationSamples(AtlasClient client) {
        LinkedList<AtlasDataSample> systemReputationHistory = client.getSystemReputationHistory();
        LinkedList<AtlasDataSample> temperatureReputationHistory = client.getTemperatureReputationHistory();

        if (systemReputationHistory.size() == properties.getMaxHistorySamples()) {
            /* Remove the first element from queue */
            systemReputationHistory.removeFirst();
        }
        /* Add new sample */
        systemReputationHistory.addLast(new AtlasDataSample(new Date(), client.getSystemReputation()));

        if (temperatureReputationHistory.size() == properties.getMaxHistorySamples()) {
            /* Remove the first element */
            temperatureReputationHistory.removeFirst();
        }
        /* Add new sample */
        temperatureReputationHistory.addLast(new AtlasDataSample(new Date(), client.getTemperatureReputation()));
    }

    private void updateFirewallIngressSamples(AtlasClient client) {
        LinkedList<AtlasDataSample> firewallRuleDroppedPktsHistory = client.getFirewallRuleDroppedPktsHistory();
        LinkedList<AtlasDataSample> firewallRulePassedPktsHistory = client.getFirewallRulePassedPktsHistory();

        if (firewallRuleDroppedPktsHistory.size() == properties.getMaxHistorySamples())
            firewallRuleDroppedPktsHistory.removeFirst();
        firewallRuleDroppedPktsHistory.addLast(new AtlasDataSample(new Date(), client.getFirewallRuleDroppedPkts()));

        if (firewallRulePassedPktsHistory.size() == properties.getMaxHistorySamples())
            firewallRulePassedPktsHistory.removeFirst();
        firewallRulePassedPktsHistory.addLast(new AtlasDataSample(new Date(), client.getFirewallRulePassedPkts()));
    }

    private void updateFirewallEgressSamples(AtlasClient client) {
        LinkedList<AtlasDataSample> firewallTxDroppedPktsHistory = client.getFirewallTxDroppedPktsHistory();
        LinkedList<AtlasDataSample> firewallTxPassedPktsHistory = client.getFirewallTxPassedPktsHistory();

        if (firewallTxDroppedPktsHistory.size() == properties.getMaxHistorySamples())
            firewallTxDroppedPktsHistory.removeFirst();
        firewallTxDroppedPktsHistory.addLast(new AtlasDataSample(new Date(), client.getFirewallTxDroppedPkts()));

        if (firewallTxPassedPktsHistory.size() == properties.getMaxHistorySamples())
            firewallTxPassedPktsHistory.removeFirst();
        firewallTxPassedPktsHistory.addLast(new AtlasDataSample(new Date(), client.getFirewallTxPassedPkts()));
    }

    private void updateClientsSamples(AtlasGateway gateway) {
        HashMap<String, AtlasClient> clients = gateway.getClients();

        for (Map.Entry<String, AtlasClient> entry : clients.entrySet()) {

            /* Update reputation history samples */
            updateClientsReputationSamples(entry.getValue());

            /* Update firewall ingress samples */
            updateFirewallIngressSamples(entry.getValue());

            /* Update firewall egress samples */
            updateFirewallEgressSamples(entry.getValue());
        }

        gatewayRepository.save(gateway);
    }

    @Override
    public synchronized void updateReputationSamples() {
        List<AtlasGateway> gateways = null;
        try {
            gateways = gatewayRepository.findAll();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        Objects.requireNonNull(gateways).forEach((gateway) -> {
            updateClientsSamples(gateway);
        });
    }
}
