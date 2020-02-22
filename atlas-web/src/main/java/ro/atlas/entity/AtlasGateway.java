package ro.atlas.entity;

import org.springframework.data.annotation.Id;

public class AtlasGateway {
	/* Collection id */
	@Id
	private String id;
	
	/* Gateway unique identifier */
	private String identity;
	
	/* Gateway pre-shared key */
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
