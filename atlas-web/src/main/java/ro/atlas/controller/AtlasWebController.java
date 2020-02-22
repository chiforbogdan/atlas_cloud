package ro.atlas.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atlas")
public class AtlasWebController {
	private static final Logger LOG = LoggerFactory.getLogger(AtlasWebController.class);
	
	@GetMapping(path="/probe")
	public String probeSaveFileController() {
		LOG.debug("StorageController is alive");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
		return dateFormat.format(new Date());
	}
}
