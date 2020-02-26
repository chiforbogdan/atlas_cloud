package ro.atlas.service;

import org.springframework.stereotype.Service;

@Service
public interface AtlasMqttService {
	/**
	 * Start MQTT service
	 */
	void start();
	
	/**
	 * Add subscribe topic
	 * @param topic Gateway topic
	 */
	void addSubscribeTopic(String topic);
}
