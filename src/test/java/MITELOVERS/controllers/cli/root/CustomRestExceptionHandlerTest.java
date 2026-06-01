package MITELOVERS.controllers.cli.root;

import MITELOVERS.controllers.exception.ApiError;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomRestExceptionHandlerTest {

    private final CustomRestExceptionHandler handler = new CustomRestExceptionHandler();

    @Test
    void shouldReturn404WhenIllegalStateException() {
        // Arrange
        IllegalStateException ex = new IllegalStateException("some state error");

        // Act
        ResponseEntity<Object> response = handler.handleIllegalState(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ApiError apiError = (ApiError) response.getBody();
        assertNotNull(apiError);
        assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
    }

    @Test
    void shouldReturn400WhenIllegalArgumentException() {
        // Arrange
        IllegalArgumentException ex = new IllegalArgumentException("some invalid argument");

        // Act
        ResponseEntity<Object> response = handler.handleIllegalArgument(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiError apiError = (ApiError) response.getBody();
        assertNotNull(apiError);
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
    }

    @Test
    void shouldReturn403WhenSecurityException() {
        // Arrange
        SecurityException ex = new SecurityException("access denied");

        // Act
        ResponseEntity<Object> response = handler.handleSecurity(ex);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ApiError apiError = (ApiError) response.getBody();
        assertNotNull(apiError);
        assertEquals(HttpStatus.FORBIDDEN, apiError.getStatus());
    }

    @Test
    void shouldReturn501WhenUnsupportedOperationException() {
        // Arrange
        UnsupportedOperationException ex = new UnsupportedOperationException("not implemented");

        // Act
        ResponseEntity<Object> response = handler.handleUnsupportedOperation(ex);

        // Assert
        assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
        ApiError apiError = (ApiError) response.getBody();
        assertNotNull(apiError);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, apiError.getStatus());
    }

    @Test
    void shouldReturn500WhenRuntimeException() {
        // Arrange
        RuntimeException ex = new RuntimeException("unexpected error");

        // Act
        ResponseEntity<Object> response = handler.handleRuntime(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiError apiError = (ApiError) response.getBody();
        assertNotNull(apiError);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiError.getStatus());
    }
}