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

        // Act
        ApiError apiError = new ApiError(status, message);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertEquals("some message", apiError.getMessage());
    }

    @Test
    void shouldCreateApiErrorWithNoArgsConstructor() {
        // Arrange + Act
        ApiError apiError = new ApiError();

        // Assert
        assertNull(apiError.getStatus());
        assertNull(apiError.getMessage());
    }
}