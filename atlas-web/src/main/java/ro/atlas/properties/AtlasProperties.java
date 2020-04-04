package ro.atlas.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "atlas-cloud")
public class AtlasProperties {
	/* MQTT cloud broker */
	private String broker;
	
	/* Gateway keep-alive counter */
	private int keepaliveCounter;
	
	/* Cloud MQTT timeout in seconds */
	private int mqttTimeout;

	/* Cloud MQTT QoS */
	private int mqttQos;

	/* The max number of samples in reputation history list */
	private int reputationMaxSamples;

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public int getKeepaliveCounter() {
		return keepaliveCounter;
	}

	public void setKeepaliveCounter(int keepaliveCounter) {
		this.keepaliveCounter = keepaliveCounter;
	}

	public int getMqttTimeout() {
		return mqttTimeout;
	}

	public void setMqttTimeout(int mqttTimeout) {
		this.mqttTimeout = mqttTimeout;
	}

	public int getMqttQos() {
		return mqttQos;
	}

	public void setMqttQos(int mqttQos) {
		this.mqttQos = mqttQos;
	}

	public int getReputationMaxSamples() {
		return reputationMaxSamples;
	}

	public void setReputationMaxSamples(int reputationMaxSamples) {
		this.reputationMaxSamples = reputationMaxSamples;
	}
}
