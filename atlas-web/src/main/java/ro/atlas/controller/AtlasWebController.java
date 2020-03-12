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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    }

    @GetMapping(path = "gateway/clients/{gateway_psk}")
    public ResponseEntity<List<AtlasClient>> getGatewayClientsList(@PathVariable("gateway_psk") String gateway_psk) {
        LOG.debug("Fetching clients for gateway with psk: " + gateway_psk);

        List<AtlasClient> clients = gatewayService.getAllClients(gateway_psk);
        if (clients == null) {
            LOG.debug("There are no clients for gateway with psk " + gateway_psk);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping(path = "gateway/client/{gateway_psk}/{client_identity}")
    public ResponseEntity<AtlasClient> getClientDetails(@PathVariable("gateway_psk") String gateway_psk, @PathVariable("client_identity") String client_identity) {
        LOG.debug("Fetching details for client with identity: " + client_identity);

        AtlasClient client = gatewayService.getClient(gateway_psk, client_identity);
        if (client == null) {
            LOG.debug("There are no client with identity " + client_identity + " within gateway with psk " + gateway_psk);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(client, HttpStatus.OK);
    }
    
    @GetMapping(path = "gateway/force-sync/{gateway_identity}")
    public void forceSync(@PathVariable("gateway_identity") String gatewayIdentity) {
        LOG.info("Force sync for gateway with identity " + gatewayIdentity);

        gatewayService.reqFullDeviceSync(gatewayIdentity);
    }

}
