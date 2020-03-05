package ro.atlas.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ro.atlas.dto.AtlasGatewayAddDto;
import ro.atlas.entity.AtlasClient;
import ro.atlas.entity.AtlasGateway;
import ro.atlas.service.AtlasGatewayService;

@RestController
@RequestMapping("/atlas")
public class AtlasWebController {
    private static final Logger LOG = LoggerFactory.getLogger(AtlasWebController.class);
    private @Autowired
    AtlasGatewayService gatewayService;

    @CrossOrigin("http://localhost:8080/")
    @GetMapping(path = "/probe")
    public String probeSaveFileController() {
        LOG.debug("Atlas controller is alive");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return dateFormat.format(new Date());
    }

    @GetMapping(path = "/gateways")
    public ResponseEntity<List<AtlasGateway>> listAllGateways() {
        LOG.debug("List all gateways GET request");
        List<AtlasGateway> gateways = gatewayService.getAllGateways();
        if (gateways.isEmpty()) {
            LOG.debug("There are no gateways");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(gateways, HttpStatus.OK);
    }

    @PostMapping(path = "gateway/add")
    public void addGateway(@RequestBody AtlasGatewayAddDto gatewayAddDto) {
        LOG.info("Add gateway with identity: " + gatewayAddDto.getIdentity() + " and psk: " + gatewayAddDto.getPsk());

        gatewayService.addGateway(gatewayAddDto);
        System.out.println("Post operation for gateway..." + gatewayAddDto.getPsk());
    }

    @GetMapping(path = "gateway/clients/{gateway_psk}")
    public ResponseEntity<List<AtlasClient>> getUser(@PathVariable("gateway_psk") String gateway_psk) {
        LOG.debug("Fetching clients for gateway with psk: " + gateway_psk);

        System.out.println("List clients for " + gateway_psk);
        List<AtlasClient> clients = gatewayService.getAllClients(gateway_psk);
        if (clients.isEmpty()) {
            LOG.debug("There are no clients for gateway with psk " + gateway_psk);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
