package ro.atlas;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ro.atlas.entity.AtlasGateway;
import ro.atlas.repository.AtlasGatewayRepository;
import ro.atlas.service.AtlasMqttService;


@Component
public class ApplicationInit implements ApplicationRunner {
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationInit.class);
 
	private @Autowired AtlasMqttService mqttService;
	private @Autowired AtlasGatewayRepository gatewayRepository;
	
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Init ATLAS cloud application...");
    
        mqttService.start();
        
        /* Subscribe to the each gateway topic */
        List<AtlasGateway> gateways = gatewayRepository.findAll();
        gateways.forEach((gateway) -> mqttService.addSubscribeTopic(gateway.getPsk()));
    }
}
