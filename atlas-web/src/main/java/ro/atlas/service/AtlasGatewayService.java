package ro.atlas.service;

import ro.atlas.dto.AtlasGatewayAddDto;
import org.springframework.stereotype.Service;
import ro.atlas.entity.AtlasGateway;

import java.util.List;

@Service
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
	
	/**
	 * Periodic keep-alive task to detect inactive gateways
	 */
	void keepaliveTask();
	
	/**
	 * Execute initial setup for gateways at application start-up
	 */
	void initGateways();
	
	/**
	 * Get a list of gateways
	 */
	List<AtlasGateway> getAllGateways();
}
