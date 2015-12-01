package ow.micropos.server.exception;

public class ResourceAlreadyExistsException extends MicroPosException {

    public ResourceAlreadyExistsException(Long id) {
        super("Resource already exists - " + id);
    }
}
