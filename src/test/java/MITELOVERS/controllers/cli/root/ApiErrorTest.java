package MITELOVERS.controllers.cli.root;
import MITELOVERS.controllers.exception.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApiErrorTest {

    @Test
    void shouldCreateApiErrorWithSingleError() {
        // Arrange
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "some message";
        String error = "some error";

        // Act
        ApiError apiError = new ApiError(status, message, error);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertEquals("some message", apiError.getMessage());
        assertEquals(1, apiError.getErrors().size());
        assertEquals("some error", apiError.getErrors().get(0));
    }

    @Test
    void shouldCreateApiErrorWithMultipleErrors() {
        // Arrange
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "some message";
        List<String> errors = List.of("first error", "second error");

        // Act
        ApiError apiError = new ApiError(status, message, errors);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertEquals("some message", apiError.getMessage());
        assertEquals(2, apiError.getErrors().size());
    }

    @Test
    void shouldCreateApiErrorWithNoArgsConstructor() {
        // Arrange + Act
        ApiError apiError = new ApiError();

        // Assert
        assertNull(apiError.getStatus());
        assertNull(apiError.getMessage());
        assertNull(apiError.getErrors());
    }
}