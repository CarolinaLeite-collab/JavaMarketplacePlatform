package MITELOVERS.controllers.exception;

import org.springframework.http.HttpStatus;

public class ApiError {

    private HttpStatus status;
    private String message;

    public ApiError() {
        super();
    }

    public ApiError(final HttpStatus status, final String message) {
        super();
        this.status = status;
        this.message = message;    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }

    public void setStatus(HttpStatus status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
}