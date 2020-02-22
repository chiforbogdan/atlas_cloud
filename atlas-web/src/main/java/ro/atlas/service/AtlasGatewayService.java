package ro.atlas.service;

import ro.atlas.dto.AtlasGatewayAddDto;

public interface AtlasGatewayService {
	/**
	 * Add gateway
	 * @param gatewayAddDto Gateway dto information
	 */
	void addGateway(AtlasGatewayAddDto gatewayAddDto);
}
