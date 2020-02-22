package ro.atlas.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.atlas.dto.AtlasGatewayAddDto;
import ro.atlas.service.AtlasGatewayService;

@RestController
@RequestMapping("/atlas")
public class AtlasWebController {
	private static final Logger LOG = LoggerFactory.getLogger(AtlasWebController.class);
	private @Autowired AtlasGatewayService gatewayService;
	
	@GetMapping(path="/probe")
	public String probeSaveFileController() {
		LOG.debug("Atlas controller is alive");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
		return dateFormat.format(new Date());
	}
	
	@PostMapping(path="gateway/add")
	public void addGateway(@RequestBody AtlasGatewayAddDto gatewayAddDto) {
		LOG.info("Add gateway with identity: " + gatewayAddDto.getIdentity() + " and psk: " + gatewayAddDto.getPsk());
		
		gatewayService.addGateway(gatewayAddDto);
	}
}
