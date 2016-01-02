package ow.micropos.server.exception;

public class UnexpectedOrderRequestException extends MicroPosException {

    public UnexpectedOrderRequestException(Long id) {
        super("An unexpected order request was encountered. See Order " + id);
    }
}
