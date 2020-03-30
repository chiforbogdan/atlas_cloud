package ro.atlas.exception;

public class GatewayNotRegisteredException extends RuntimeException {

    public GatewayNotRegisteredException(String gatewayIdentity) {
        super(String.format("Gateway with identity %s is not online! Synchronization is not possible!", gatewayIdentity));
    }
}
