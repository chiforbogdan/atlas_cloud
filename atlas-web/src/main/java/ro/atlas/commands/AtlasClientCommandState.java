package ro.atlas.commands;

public enum AtlasClientCommandState {
	/* Client command is not delivered to gateway yet */
	ATLAS_CMD_CLIENT_DELIVERING_TO_GATEWAY(0),
	/* Client command is not delivered to client yet (but is delivered to gateway) */
	ATLAS_CMD_CLIENT_DELIVERING_TO_CLIENT(1),
	/* Client command was executed by the client */
	ATLAS_CMD_CLIENT_EXECUTED_BY_CLIENT(2);
	
	/* Client command state */
	private int state;
	
	AtlasClientCommandState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int cmdState) {
		this.state = cmdState;
	}
	
}
