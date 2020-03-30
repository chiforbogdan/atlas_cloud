package ro.atlas.exception;

public class GatewayNotFoundException extends RuntimeException {

    public GatewayNotFoundException(String gatewayIdentity) {
        super(String.format("Gateway with identity %s not found!", gatewayIdentity));
    }
}
