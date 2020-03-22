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
     * Get gateway from db
     * @param gw_identity gateway identity
     * @return AtlasGateway
     */
    AtlasGateway getGateway(String gw_identity);

    /**
     * Get all the clients of a gateway
     * @param gw_identity gateway identity
     * @return list of clients
     */
    List<AtlasClient> getAllClients(String gw_identity);

    /**
     * Get client details
     * @param gw_identity gateway psk
     * @param cl_identity client identity
     */
    AtlasClient getClient(String gw_identity, String cl_identity);

    /**
     * Delete gateway from db
     * @param gw gateway to be deleted
     */
    void deleteGateway(AtlasGateway gw);

    /**
     * Delete client from db
     * @param gateway
     * @param cl_identity
     */
    void deleteClient(AtlasGateway gateway, String cl_identity);

    /**
     * Request a full device sync for a gateway
     * @param gatewayIdentity Gateway identity
     */
    void reqFullDeviceSync(String gatewayIdentity);

}
