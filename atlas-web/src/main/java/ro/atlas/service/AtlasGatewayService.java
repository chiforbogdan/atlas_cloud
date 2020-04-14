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
     * @param gatewayIdentity gateway identity
     * @return AtlasGateway
     */
    AtlasGateway getGateway(String gatewayIdentity);

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
     * @param gatewayIdentity gateway psk
     * @param clientIdentity  client identity
     */
    AtlasClient getClient(String gatewayIdentity, String clientIdentity);

    /**
     * Delete gateway from db
     *
     * @param gateway gateway to be deleted
     */
    void deleteGateway(AtlasGateway gateway);

    /**
     * Delete client from db
     *
     * @param gateway        Gateway
     * @param clientIdentity Client identity
     */
    void deleteClient(AtlasGateway gateway, String clientIdentity);

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

    /**
     * Edit client's alias
     */
    void updateClientAlias(String gatewayIdentity, String clientIdentity, String alias);
}
