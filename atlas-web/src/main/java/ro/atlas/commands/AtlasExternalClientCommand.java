package ro.atlas.commands;

public class AtlasExternalClientCommand {
	/* Client identity */
	private String clientIdentity;
	/* Command type */
	private AtlasClientCommandType type;
	/* Command payload */
	private String payload;
	/* Command identifier */
	private int identifier;

	public AtlasExternalClientCommand(String clientIdentity, AtlasClientCommand clientCommand) {
		this.clientIdentity = clientIdentity;
		this.type = clientCommand.getType();
		this.payload = clientCommand.getPayload();
		this.identifier = clientCommand.getIdentifier();
	}

	public String getClientIdentity() {
		return clientIdentity;
	}

	public AtlasClientCommandType getType() {
		return type;
	}

	public String getPayload() {
		return payload;
	}

	public int getIdentifier() {
		return identifier;
	}
}
