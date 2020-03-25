package ro.atlas.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ro.atlas.service.AtlasGatewayService;
import ro.atlas.service.CronService;

@Component
public class CronServiceImpl implements CronService {
	private static final Logger LOG = LoggerFactory.getLogger(CronServiceImpl.class);
	private @Autowired AtlasGatewayService gatewayService;
	
	@Scheduled(fixedRateString = "${atlas-cloud.keepalive-task-interval-min}")
	@Override
	public void keepaliveTask() {
		LOG.info("Run keep-alive task for gateways");
		
		gatewayService.keepaliveTask();
	}

}
