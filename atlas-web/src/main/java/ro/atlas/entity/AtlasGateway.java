package ro.atlas.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AtlasGateway {
	/* Document id */
	@Id
	private String id;
	
	/* Gateway unique identifier */
	@Indexed(name = "identity_index_unique", unique = true)
	private String identity;
	
	/* Gateway pre-shared key */
	@Indexed(name = "psk_index_unique", unique = true)
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
