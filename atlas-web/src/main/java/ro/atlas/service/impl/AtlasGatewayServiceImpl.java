package ro.atlas.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.atlas.commands.AtlasCommand;
import ro.atlas.commands.AtlasCommandType;
import ro.atlas.dto.AtlasGatewayAddDto;
import ro.atlas.entity.AtlasClient;
import ro.atlas.entity.AtlasGateway;
import ro.atlas.repository.AtlasGatewayRepository;
import ro.atlas.service.AtlasGatewayService;

@Component
public class AtlasGatewayServiceImpl implements AtlasGatewayService {

    private static final Logger LOG = LoggerFactory.getLogger(AtlasGatewayServiceImpl.class);

    /* Keep-alive counter value */
    private static final int ATLAS_KEEPALIVE_COUNTER = 3;
    
    /* Publish-subscribe topic suffix which allows full-duplex communication with the gateway */
    private static final String ATLAS_TO_GATEWAY_TOPIC = "-to-gateway";
    private static final String ATLAS_TO_CLOUD_TOPIC = "-to-cloud";

    private @Autowired
    AtlasGatewayRepository gatewayRepository;
    private @Autowired
    AtlasMqttServiceImpl mqttService;

    @Transactional
    @Override
    public void addGateway(AtlasGatewayAddDto gatewayAddDto) {
        LOG.info("Adding gateway info...");

        /* Create initial gateway state */
        AtlasGateway gateway = new AtlasGateway();
        gateway.setAlias(gatewayAddDto.getAlias());
        gateway.setIdentity(gatewayAddDto.getIdentity());
        gateway.setPsk(gatewayAddDto.getPsk());
        gateway.setClients(new HashMap<>());

        /* Add gateway to database */
        gateway = gatewayRepository.save(gateway);

        /* Subscribe to the gateway topic */
        mqttService.addSubscribeTopic(gateway.getPsk() +  ATLAS_TO_CLOUD_TOPIC);
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
            gateways = gatewayRepository.findAll();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        return gateways;
    }

    @Override
    public List<AtlasClient> getAllClients(String psk) {
        HashMap<String, AtlasClient> clients = null;
        try {
            clients = gatewayRepository.findByPsk(psk).getClients();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        if (clients.isEmpty())
            return null;

        return new ArrayList<AtlasClient>(clients.values());
    }

    @Override
    public AtlasClient getClient(String psk, String identity) {
        AtlasGateway gateway = null;
        try {
            gateway = gatewayRepository.findByPsk(psk);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        if (gateway == null)
            return null;

        return gateway.getClients().get(identity);
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
        if (client == null)
            gateway.getClients().put(clientInfo.getIdentity(), clientInfo);
        else
            client.updateInfo(clientInfo);

        gatewayRepository.save(gateway);
    }

    @Transactional
    private void registerNow(AtlasGateway gateway) {
        LOG.info("Set register state for gateway with identity " + gateway.getIdentity());

        gateway.setRegistered(true);
        gateway.setKeepaliveCounter(ATLAS_KEEPALIVE_COUNTER);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        gateway.setLastRegistertTime(dateFormat.format(new Date()));

        gatewayRepository.save(gateway);
    }

    @Transactional
    private void keepaliveNow(AtlasGateway gateway) {
        LOG.info("Set keep-alive state for gateway with identity " + gateway.getIdentity());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        gateway.setLastKeepaliveTime(dateFormat.format(new Date()));
        gateway.setKeepaliveCounter(ATLAS_KEEPALIVE_COUNTER);

        gatewayRepository.save(gateway);
    }

    @Transactional
    private void decrementKeepalive(AtlasGateway gateway) {
        LOG.info("Decrement keep-alive counter for gateway with identity " + gateway.getIdentity());

        if (gateway.getKeepaliveCounter() == 0) {
            LOG.info("Gateway with identity " + gateway.getIdentity() + " becomes inactive");
            gateway.setRegistered(false);
        } else
            gateway.setKeepaliveCounter(gateway.getKeepaliveCounter() - 1);

        gatewayRepository.save(gateway);
    }

    @Override
    public synchronized void keepaliveTask() {
        LOG.info("Run periodic keep-alive task to detect inactive gateways");

        List<AtlasGateway> gateways = gatewayRepository.findAll();
        gateways.forEach((gateway) -> {
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
        
        gatewayRepository.save(gateway);
        
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
        reqFullDeviceSync(gateway.getIdentity());
    }

    @Override
    public synchronized void initGateways() {
        LOG.info("Init gateways at application start-up");

        List<AtlasGateway> gateways = gatewayRepository.findAll();
        gateways.forEach((gateway) -> {
            initGateway(gateway);
        });
    }

	@Override
	public void reqFullDeviceSync(String gatewayIdentity) {
		 AtlasGateway gateway = null;

		try {
			gateway = gatewayRepository.findByIdentity(gatewayIdentity);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		if (gateway == null) {
			LOG.error("Cannot find gateway find identity " + gatewayIdentity);
			return;
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
}
