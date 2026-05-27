package MITELOVERS.controllers.exception;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;

public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError() {
        super();
    }

    public ApiError(final HttpStatus status, final String message, final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(final HttpStatus status, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        this.errors = Arrays.asList(error);
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public List<String> getErrors() { return errors; }

    public void setStatus(HttpStatus status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}