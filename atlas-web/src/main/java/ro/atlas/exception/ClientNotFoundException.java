package ro.atlas.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String clientIdentity) {
        super(String.format("Client with identity %s nor found!", clientIdentity));
    }
}
