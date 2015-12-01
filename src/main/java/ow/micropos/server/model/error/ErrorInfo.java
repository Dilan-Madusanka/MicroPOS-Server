package ow.micropos.server.model.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorInfo {

    String status;

    String message;

    public ErrorInfo(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorInfo(HttpStatus status, Exception e) {
        this.status = status.value() + " " + status.getReasonPhrase();
        this.message = e.getMessage();
    }

}
