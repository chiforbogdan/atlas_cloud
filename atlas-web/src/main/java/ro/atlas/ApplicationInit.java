package ro.atlas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ro.atlas.service.AtlasMqttService;


@Component
public class ApplicationInit implements ApplicationRunner {
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationInit.class);
 
	@Autowired AtlasMqttService mqttService;
	
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Init ATLAS cloud application...");
    
        mqttService.start();
    }
}
