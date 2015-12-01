package ow.micropos.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ow.micropos.server.exception.*;
import ow.micropos.server.model.error.ErrorInfo;

@RestController
@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserPermissionException.class)
    public ErrorInfo userPermissionException(Exception ex) {
        return new ErrorInfo(HttpStatus.FORBIDDEN, ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ErrorInfo internalErrorException(Exception ex) {
        return new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidParameterException.class)
    public ErrorInfo invalidParameterException(Exception ex) {
        return new ErrorInfo(HttpStatus.BAD_REQUEST, ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorInfo notFoundException(Exception ex) {
        return new ErrorInfo(HttpStatus.NOT_FOUND, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MicroPosException.class)
    public ErrorInfo expectedException(Exception ex) {
        return new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo unexpectedException(Exception ex) {
        return new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, ex);

    }

}
