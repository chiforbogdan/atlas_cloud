package ro.atlas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ro.atlas.dto.AtlasClientSummaryDto;
import ro.atlas.dto.AtlasGatewayAddDto;
import ro.atlas.entity.AtlasClient;
import ro.atlas.entity.AtlasGateway;

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
     * @param topic   Publish-subscribe topic on which the message was received
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
     *
     * @param gw_identity gateway identity
     * @return AtlasGateway
     */
    AtlasGateway getGateway(String gw_identity);

    /**
     * Get all the clients summary of a gateway
     *
     * @param gatewayIdentity gateway identity
     * @return list of clients summary
     */
    List<AtlasClientSummaryDto> getAllClientsSummary(String gatewayIdentity);

    /**
     * Get client details
     *
     * @param gw_identity gateway psk
     * @param cl_identity client identity
     */
    AtlasClient getClient(String gw_identity, String cl_identity);

    /**
     * Delete gateway from db
     *
     * @param gw gateway to be deleted
     */
    void deleteGateway(AtlasGateway gw);

    /**
     * Delete client from db
     *
     * @param gateway     Gateway
     * @param cl_identity Client identity
     */
    void deleteClient(AtlasGateway gateway, String cl_identity);

    /**
     * Request a full device sync for a gateway
     *
     * @param gateway Gateway
     */
    void reqFullDeviceSync(AtlasGateway gateway);

    /**
     * Add a new sample in reputation history samples
     */
    void updateReputationSamples();

}
