package ro.atlas.service.impl;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.atlas.commands.AtlasCommandType;
import ro.atlas.dto.AtlasGatewayAddDto;
import ro.atlas.entity.AtlasClient;
import ro.atlas.entity.AtlasGateway;
import ro.atlas.repository.AtlasGatewayRepository;
import ro.atlas.service.AtlasGatewayService;

@Component
public class AtlasGatewayServiceImpl implements AtlasGatewayService {

	private static final Logger LOG = LoggerFactory.getLogger(AtlasGatewayServiceImpl.class);
	
	private @Autowired AtlasGatewayRepository gatewayRepository;
	private @Autowired AtlasMqttServiceImpl mqttService;
	
	@Override
	public void addGateway(AtlasGatewayAddDto gatewayAddDto) {
		LOG.info("Adding gateway info...");
		
		/* Create initial gateway state */
		AtlasGateway gateway = new AtlasGateway();
		gateway.setIdentity(gatewayAddDto.getIdentity());
		gateway.setPsk(gatewayAddDto.getPsk());
		gateway.setClients(new HashMap<>());
	
		/* Add gateway to database */
		gateway = gatewayRepository.save(gateway);
		
		/* Subscribe to the gateway topic */
		mqttService.addSubscribeTopic(gateway.getPsk());
	}

	@Transactional
	@Override
	public void messageReceived(String psk, byte[] payload) {
		AtlasGateway gateway = null;
		
		if (payload == null || payload.length == 0)
			return;
		
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
			String cmdPayload = jsonObject.getString(AtlasCommandType.ATLAS_CMD_PAYLOAD_FIELDNAME);
			ObjectMapper mapper = new ObjectMapper();

			/* Check command type */
			if (jsonObject.getString(AtlasCommandType.ATLAS_CMD_TYPE_FIELDNAME)
					.equalsIgnoreCase(AtlasCommandType.ATLAS_CMD_CLIENT_INFO_UPDATE.getCommandType())) {
				AtlasClient clientInfo = mapper.readValue(cmdPayload.getBytes(), AtlasClient.class);
				AtlasClient client = gateway.getClients().get(clientInfo.getIdentity());
				if (client == null)
					gateway.getClients().put(clientInfo.getIdentity(), clientInfo);
				else
					client.updateInfo(clientInfo);
				
				gatewayRepository.save(gateway);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
