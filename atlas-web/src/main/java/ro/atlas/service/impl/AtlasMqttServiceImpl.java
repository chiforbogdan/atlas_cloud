package ro.atlas.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.atlas.service.AtlasGatewayService;
import ro.atlas.service.AtlasMqttService;

@Component
public class AtlasMqttServiceImpl implements AtlasMqttService, IMqttMessageListener, MqttCallback {

	private static final Logger LOG = LoggerFactory.getLogger(AtlasMqttServiceImpl.class);
	private static final int ATLAS_MQTT_TIMEOUT_SEC = 60;
	private static final int ATLAS_MQTT_QOS_2 = 2;
	private MqttClient client;
	private Set<String> subscribeTopics = new HashSet<>();
	private String broker = "tcp://127.0.0.1:1883";
	private String clientId;
	private MemoryPersistence persistence = new MemoryPersistence();
	
	private @Autowired AtlasGatewayService gatewayService;
	
	@Override
	public void start() {
		LOG.info("Start MQTT service...");
		
		/* Generate unique client id */
		clientId = UUID.randomUUID().toString();
		
		/* Create connection options */
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(ATLAS_MQTT_TIMEOUT_SEC);
		
		/* Create MQTT client */
		try {
			LOG.info("Connecting to broker: "+ broker + " with client id " + clientId);
			
			client = new MqttClient(broker, clientId, persistence);
			client.setCallback(this);
			client.connect(options);
			
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOG.info("Received message on topic " + topic);
		
		gatewayService.messageReceived(topic, message.getPayload());
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOG.info("Connection to server is lost");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		LOG.info("Message delivery is complete");
	}

	@Override
	public void
	addSubscribeTopic(String topic) {
		if (!subscribeTopics.add(topic))
			return;
		
		if (client.isConnected()) {
			LOG.info("Subscribe to gateway topic: " + topic);
			try {
				client.subscribe(topic);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void publish(String topic, String message) {
		try {
			client.publish(topic, message.getBytes(), ATLAS_MQTT_QOS_2, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
