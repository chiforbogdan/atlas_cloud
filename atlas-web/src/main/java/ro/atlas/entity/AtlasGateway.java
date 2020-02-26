package ro.atlas.entity;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AtlasGateway {
	/* Document id */
	@Id
	private String id;
	
	/* Gateway unique identifier */
	//@Indexed(unique = true)
	private String identity;
	
	/* Gateway pre-shared key */
	//@Indexed(unique = true)
	private String psk;

	/* Client information */
	private HashMap<String, AtlasClient> clients;
	
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

	public HashMap<String, AtlasClient> getClients() {
		return clients;
	}

	public void setClients(HashMap<String, AtlasClient> clients) {
		this.clients = clients;
	}
}
