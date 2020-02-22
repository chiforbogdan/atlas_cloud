package ro.atlas.dto;

public class AtlasGatewayAddDto {
	/* Gateway identity */
	private String identity;
	
	/* Gateway PSK */
	private String psk;
	
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getPsk() {
		return psk;
	}

	public void setPsk(String psk) {
		this.psk = psk;
	}
}
