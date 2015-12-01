package ow.micropos.server.exception;

public class ResourceNotFoundException extends MicroPosException {

    public ResourceNotFoundException(Long id) {
        super("Resource not found - " + id);
    }

}
