package ro.atlas.dto;

public class AtlasGatewayAddDto {
    /* Gateway identity */
    private String identity;

    /* Gateway PSK */
    private String psk;

    public AtlasGatewayAddDto() {
        this.identity = "";
        this.psk = "";
    }

    public AtlasGatewayAddDto(String identity, String psk) {
        this.identity = identity;
        this.psk = psk;
    }

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
