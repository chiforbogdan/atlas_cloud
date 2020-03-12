package ro.atlas.service;

import ro.atlas.dto.AtlasGatewayAddDto;
import org.springframework.stereotype.Service;
import ro.atlas.entity.AtlasClient;
import ro.atlas.entity.AtlasGateway;

import java.util.List;

@Service
public interface AtlasGatewayService {
    /**
     * Add gateway
     *
     * @param gatewayAddDto Gateway dto information
     */
    void addGateway(AtlasGatewayAddDto gatewayAddDto);

    /**
     * Callback for handling gateway message
     *
     * @param topic     Publish-subscribe topic on which the message was received
     * @param payload Gateway message payload
     */
    void messageReceived(String topic, byte[] payload);

    /**
     * Periodic keep-alive task to detect inactive gateways
     */
    void keepaliveTask();

    /**
     * Execute initial setup for gateways at application start-up
     */
    void initGateways();

    /**
     * Get the list of all gateways from db
     */
    List<AtlasGateway> getAllGateways();

    /**
     * Get all the clients for a gateway with psk
     */
    List<AtlasClient> getAllClients(String psk);

    /**
     * Get client details
     * @param psk gateway psk
     * @param identity client identity
     */
    AtlasClient getClient(String psk, String identity);
    
    /**
     * Request a full device sync for a gateway
     * @param gatewayIdentity Gateway identity
     */
    void reqFullDeviceSync(String gatewayIdentity);
}
