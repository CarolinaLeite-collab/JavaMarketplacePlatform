package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreIdTest {

    @Test
    void constructorValidNameCreatesGenreId() {
        // Arrange
        String name = "Fiction";

        // SUT
        GenreId _sut = new GenreId(name);

        // Act
        GenreId result = _sut;

        // Assert
        assertNotNull(result);
    }

    @Test
    void constructorNullNameThrowsIllegalArgumentException() {
        // Arrange
        String name = null;

        // SUT
        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new GenreId(name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorBlankNameThrowsIllegalArgumentException() {
        // Arrange
        String name = "   ";

        // SUT
        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new GenreId(name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorEmptyNameThrowsIllegalArgumentException() {
        // Arrange
        String name = "";

        // SUT

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new GenreId(name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorNormalisesNameToUpperCase() {
        // Arrange
        String name = "fiction";

        // SUT
        GenreId _sut = new GenreId(name);

        // Act
        String result = _sut.toString();

        // Assert
        assertEquals("FICTION", result);
    }

    @Test
    void equalsSameNameCaseInsensitiveReturnsTrue() {
        // Arrange
        GenreId genreId1 = new GenreId("Fiction");
        GenreId genreId2 = new GenreId("fiction");

        // SUT
        GenreId _sut = genreId1;

        // Act
        boolean result = _sut.equals(genreId2);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsDifferentNameReturnsFalse() {
        // Arrange
        GenreId genreId1 = new GenreId("Fiction");
        GenreId genreId2 = new GenreId("Horror");

        // SUT
        GenreId _sut = genreId1;

        // Act
        boolean result = _sut.equals(genreId2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // Arrange
        GenreId genreId = new GenreId("Fiction");

        // SUT
        GenreId _sut = genreId;

        // Act
        boolean result = _sut.equals(genreId);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        GenreId genreId = new GenreId("Fiction");

        // SUT
        GenreId _sut = genreId;

        // Act
        boolean result = _sut.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameNameReturnsSameHash() {
        // Arrange
        GenreId genreId1 = new GenreId("Fiction");
        GenreId genreId2 = new GenreId("fiction");

        // SUT
        GenreId _sut = genreId1;

        // Act
        int result = _sut.hashCode();

        // Assert
        assertEquals(result, genreId2.hashCode());
    }

    @Test
    void hashCodeReturnsIdHashCode() {
        // Arrange
        GenreId genreId = new GenreId("Fiction");

        // SUT
        GenreId _sut = genreId;

        // Act
        int result = _sut.hashCode();

        // Assert
        assertEquals("FICTION".hashCode(), result);
    }

    @Test
    void toStringReturnsNormalisedName() {
        // Arrange
        String name = "Fiction";

        // SUT
        GenreId _sut = new GenreId(name);

        // Act
        String result = _sut.toString();

        // Assert
        assertEquals("FICTION", result);
    }

}

