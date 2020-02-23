package ro.atlas.commands;

public class AtlasCommand {
	private AtlasCommandType commandType;
	
	private Object commandPayload;

	public AtlasCommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(AtlasCommandType commandType) {
		this.commandType = commandType;
	}

	public Object getCommandPayload() {
		return commandPayload;
	}

	public void setCommandPayload(Object commandPayload) {
		this.commandPayload = commandPayload;
	}
}
