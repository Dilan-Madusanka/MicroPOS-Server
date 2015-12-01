package ow.micropos.server.exception;

public class UnpaidOrdersException extends MicroPosException {

    public UnpaidOrdersException() {
        super("There are still unpaid orders.");
    }
}
