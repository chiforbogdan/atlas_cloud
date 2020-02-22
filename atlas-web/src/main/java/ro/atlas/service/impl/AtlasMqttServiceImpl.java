package ro.atlas.service.impl;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ro.atlas.service.AtlasMqttService;

@Component
public class AtlasMqttServiceImpl implements AtlasMqttService, IMqttMessageListener, MqttCallback {

	private static final Logger LOG = LoggerFactory.getLogger(AtlasMqttServiceImpl.class);
	private static final int ATLAS_MQTT_TIMEOUT_SEC = 60;
	private MqttClient client;
	
	private String broker = "tcp://127.0.0.1:1883";
	String clientId;
	MemoryPersistence persistence = new MemoryPersistence();
	
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
			
			client.subscribe("atlas/test1", this);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOG.info("Received message on topic " + topic);
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOG.info("Connection to server is lost");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		LOG.info("Message delivery is complete");
	}

}
