package ro.atlas.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "atlas-cloud")
public class AtlasProperties {
	/* MQTT cloud broker */
	private String broker;
	
	/* Cloud MQTT password file */
	private String passwordFile;
	
	/* Cloud MQTT password utility tool */
	private String passwordTool;
	
	/* Cloud MQTT broker PID file */
	private String brokerPidFile;
	
	/* Temporary directory */
	private String tmpDir;
	
	/* Gateway keep-alive counter */
	private int keepaliveCounter;
	
	/* Cloud MQTT timeout in seconds */
	private int mqttTimeout;

	/* Cloud MQTT QoS */
	private int mqttQos;

	/* The max number of samples in history lists */
	private int maxHistorySamples;

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

	public String getPasswordFile() {
		return passwordFile;
	}

	public void setPasswordFile(String passwordFile) {
		this.passwordFile = passwordFile;
	}

	public String getPasswordTool() {
		return passwordTool;
	}

	public void setPasswordTool(String passwordTool) {
		this.passwordTool = passwordTool;
	}

	public String getTmpDir() {
		return tmpDir;
	}

	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	public String getBrokerPidFile() {
		return brokerPidFile;
	}

	public void setBrokerPidFile(String brokerPidFile) {
		this.brokerPidFile = brokerPidFile;
	}
	
	public int getMaxHistorySamples() {
		return maxHistorySamples;
	}

	public void setMaxHistorySamples(int maxHistorySamples) {
		this.maxHistorySamples = maxHistorySamples;
	}
}
