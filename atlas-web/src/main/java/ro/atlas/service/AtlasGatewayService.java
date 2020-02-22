package ro.atlas.service;

import ro.atlas.dto.AtlasGatewayAddDto;

public interface AtlasGatewayService {
	/**
	 * Add gateway
	 * @param gatewayAddDto Gateway dto information
	 */
	void addGateway(AtlasGatewayAddDto gatewayAddDto);
	
	/**
	 * Callback for handling gateway message
	 * @param psk Gateway pre-shared key
	 * @param payload Gateway message payload
	 */
	void messageReceived(String psk, byte[] payload);
}
