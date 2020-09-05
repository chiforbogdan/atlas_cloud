package ro.atlas.commands;

public enum AtlasGatewayCommandType {
	
	/* Commands sent from gateway to cloud */
	ATLAS_CMD_GATEWAY_CLIENT_INFO_UPDATE("ATLAS_CMD_GATEWAY_CLIENT_INFO_UPDATE"),
	ATLAS_CMD_GATEWAY_REGISTER("ATLAS_CMD_GATEWAY_REGISTER"),
	ATLAS_CMD_GATEWAY_KEEPALIVE("ATLAS_CMD_GATEWAY_KEEPALIVE"),
	ATLAS_CMD_GATEWAY_CLIENT_ACK("ATLAS_CMD_GATEWAY_CLIENT_ACK"),
	ATLAS_CMD_GATEWAY_CLIENT_DONE("ATLAS_CMD_GATEWAY_CLIENT_DONE"),
	
	/* Commands sent from cloud to gateway */
	ATLAS_CMD_GATEWAY_REGISTER_REQUEST("ATLAS_CMD_GATEWAY_REGISTER_REQUEST"),
	ATLAS_CMD_GATEWAY_GET_ALL_DEVICES("ATLAS_CMD_GATEWAY_GET_ALL_DEVICES"),
	ATLAS_CMD_GATEWAY_CLIENT("ATLAS_CMD_GATEWAY_CLIENT");
	
	public static final String ATLAS_CMD_TYPE_FIELDNAME = "commandType";
	public static final String ATLAS_CMD_PAYLOAD_FIELDNAME = "commandPayload";
	
	private String commandType;

	AtlasGatewayCommandType(String commandType) {
		this.commandType = commandType;
	}

	public String getCommandType() {
		return commandType;
	}
}
