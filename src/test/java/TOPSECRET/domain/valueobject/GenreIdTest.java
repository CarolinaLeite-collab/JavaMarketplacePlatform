package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreIdTest {

    @Test
    void constructorValidNameCreatesGenreId() {
        // Arrange
        String name = "Fiction";

        // Act
        GenreId genreId = new GenreId(name);

        // Assert
        assertNotNull(genreId);
    }

    @Test
    void constructorNullNameThrowsIllegalArgumentException() {
        // Arrange
        String name = null;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GenreId(name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorBlankNameThrowsIllegalArgumentException() {
        // Arrange
        String name = "   ";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GenreId(name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorEmptyNameThrowsIllegalArgumentException() {
        // Arrange
        String name = "";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GenreId(name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorNormalisesNameToUpperCase() {
        // Arrange
        String name = "fiction";

        // Act
        GenreId genreId = new GenreId(name);

        // Assert
        assertEquals("FICTION", genreId.toString());
    }

    @Test
    void equalsSameNameCaseInsensitiveReturnsTrue() {
        // Arrange
        GenreId genreId1 = new GenreId("Fiction");

        // Act
        GenreId genreId2 = new GenreId("fiction");

        // Assert
        assertEquals(genreId1, genreId2);
    }

    @Test
    void equalsDifferentNameReturnsFalse() {
        // Arrange
        GenreId genreId1 = new GenreId("Fiction");

        // Act
        GenreId genreId2 = new GenreId("Horror");

        // Assert
        assertNotEquals(genreId1, genreId2);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // Arrange
        GenreId genreId = new GenreId("Fiction");

        // Act
        boolean result = genreId.equals(genreId);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        GenreId genreId = new GenreId("Fiction");

        // Act
        boolean result = genreId.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameNameReturnsSameHash() {
        // Arrange
        GenreId genreId1 = new GenreId("Fiction");

        // Act
        GenreId genreId2 = new GenreId("fiction");

        // Assert
        assertEquals(genreId1.hashCode(), genreId2.hashCode());
    }

    @Test
    void toStringReturnsNormalisedName() {
        // Arrange
        String name = "Fiction";

        // Act
        GenreId genreId = new GenreId(name);

        // Assert
        assertEquals("FICTION", genreId.toString());
    }

}