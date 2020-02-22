package ro.atlas.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.atlas.dto.AtlasGatewayAddDto;
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
	
		/* Add gateway to database */
		gateway = gatewayRepository.save(gateway);
		
		/* Subscribe to the gateway topic */
		mqttService.addSubscribeTopic(gateway.getPsk());
	}

	@Override
	public void messageReceived(String psk, byte[] payload) {
		AtlasGateway gateway = null;
		
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
	}
}
