package ro.atlas.dto;

import ro.atlas.commands.AtlasClientCommand;
import ro.atlas.commands.AtlasClientCommandType;

public class AtlasClientCommandDto {
	/* Client identity */
	private String clientIdentity;
	/* Command type */
	private AtlasClientCommandType type;
	/* Command payload */
	private String payload;
	/* Command sequence number */
	private int seqNo;

	public AtlasClientCommandDto() {
	}

	public AtlasClientCommandDto(String clientIdentity, AtlasClientCommand clientCommand) {
		this.clientIdentity = clientIdentity;
		this.type = clientCommand.getType();
		this.payload = clientCommand.getPayload();
		this.seqNo = clientCommand.getSeqNo();
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

	public int getSeqNo() {
		return seqNo;
	}
}
