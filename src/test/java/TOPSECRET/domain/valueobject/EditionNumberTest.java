package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditionNumberTest {

    @Test
    void shouldCreateEditionNumberSuccessfully() {
        // Arrange & Act
        EditionNumber editionNumber = new EditionNumber(1);
    }

    @Test
    void shouldAllowAnyPositiveNumber() {
        // Act
        EditionNumber editionNumber = new EditionNumber(10);

        // Assert
        assertEquals(10, editionNumber.getValue());
    }

    @Test
    void shouldThrowExceptionWhenZero() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new EditionNumber(0)
        );

        assertEquals("Edition number must be positive", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNegative() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new EditionNumber(-5)
        );

        assertEquals("Edition number must be positive", exception.getMessage());
    }
}