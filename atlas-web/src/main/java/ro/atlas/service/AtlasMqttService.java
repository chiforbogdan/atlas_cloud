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
	
	/**
	 * Publish a message to a topic
	 * @param topic Publish-subscribe topic
	 * @param message Message payload
	 */
	void publish(String topic, String message);
	
	/**
	 * Indicates if the underlying connection was broken and if all the topics should
	 * be subscribed again
	 * @return True if the topics should be subscribed again, false otherwise
	 */
	boolean shouldSubscribeAllTopics();
}
