package ow.micropos.server.exception;

public class UnsafeUpdateException extends MicroPosException {
    public UnsafeUpdateException(Long id) {
        super("Resource is referenced - " + id);
    }
}
