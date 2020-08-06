package ro.atlas.commands;

import java.util.Date;

public class AtlasClientCommand {
	/* Command type */
	private AtlasClientCommandType type;
	
	/* Command payload */
	private String payload;
	
	/* Command creation time */
	private Date creationTime;
	
	/* Command execution time */
	private Date executionTime;
	
	/* Command state */
	private AtlasClientCommandState state;
	
	/* Command identifier (must be incrementally set). This is also used as sequence number. */
	private int identifier;
	
	public AtlasClientCommandType getType() {
		return type;
	}
	
	public void setType(AtlasClientCommandType type) {
		this.type = type;
	}
	
	public String getPayload() {
		return payload;
	}
	
	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Date executionTime) {
		this.executionTime = executionTime;
	}

	public AtlasClientCommandState getState() {
		return state;
	}

	public void setState(AtlasClientCommandState state) {
		this.state = state;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
}
