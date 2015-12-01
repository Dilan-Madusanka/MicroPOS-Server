package ow.micropos.server.exception;

public class ResourceBadRequestException extends MicroPosException {

    public ResourceBadRequestException(Long id) {
        super("Resource bad request - " + id);
    }

}
