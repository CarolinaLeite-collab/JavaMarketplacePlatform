package MITELOVERS.domain.genre;

import MITELOVERS.domain.valueobject.GenreId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    @Test
    void constructorShouldBuildGenre() {
        // Arrange
        String name = "Science Fiction";

        // SUT
        Genre _sut = new Genre(name);

        // Act
        Genre result = _sut;

        // Assert
        assertNotNull(result);
    }

    @Test
    void constructorShouldTrimGenreName() {
        // Arrange
        String name = " Science Fiction  ";

        // SUT
        Genre _sut = new Genre(name);

        // Act
        String result = _sut.getGenre();

        // Assert
        assertEquals("Science Fiction", result);
    }

    @Test
    void constructorShouldThrowExceptionWhenGenreNameIsBlank() {
        // Arrange
        String name = "  ";

        // SUT
        // Act
        Exception result = assertThrows(IllegalArgumentException.class, () -> new Genre(name));

        // Assert
        assertNotNull(result);
    }

    @Test
    void constructorShouldThrowWhenGenreNameIsNull() {
        // Arrange
        String name = null;

        // SUT

        // Act
        Exception result = assertThrows(NullPointerException.class, () -> new Genre(name));

        // Assert
        assertNotNull(result);
    }

    @Test
    void identityReturnsNonNullGenreId() {
        // Arrange
        String name = "Science Fiction";

        // SUT
        Genre _sut = new Genre(name);

        // Act
        GenreId id = _sut.identity();

        // Assert
        assertNotNull(id);
    }

    @Test
    void sameAsSameInstanceReturnsTrue() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // SUT
        Genre _sut = genre;

        // Act
        boolean result = _sut.sameAs(genre);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsSameNameReturnsTrue() {
        // Arrange
        Genre genre1 = new Genre("Science Fiction");
        Genre genre2 = new Genre("Science FictIon");

        // SUT
        Genre _sut = genre1;

        // Act
        boolean result = _sut.sameAs(genre2);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsDifferentNameReturnsFalse() {
        // Arrange
        Genre genre1 = new Genre("Science Fiction");

        // SUT
        Genre _sut = genre1;

        // Act
        Genre genre2 = new Genre("Romance");

        // Assert
        assertFalse(_sut.sameAs(genre2));
    }

    @Test
    void sameAsNullReturnsFalse() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // SUT
        Genre _sut = genre;

        // Act
        boolean result = _sut.sameAs(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // SUT
        Genre _sut = genre;

        // Act
        boolean result = _sut.sameAs("Science Fiction");

        // Assert
        assertFalse(result);
    }

    @Test
    void genreNotEqualsToNull() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // SUT
        Genre _sut = genre;

        // Act
        boolean result = _sut.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void genreWithSameName() {
        // Arrange
        Genre genre = new Genre("Romance ");
        Genre genre2 = new Genre("ROMANCE");

        // SUT
        Genre _sut = genre;

        // Act
        boolean result = _sut.equals(genre2);

        // Assert
        assertTrue(result);
    }

    @Test
    void genreEqualsItself() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // SUT
        Genre _sut = genre;

        // Act
        boolean result = _sut.equals(genre);

        // Assert
        assertTrue(result);
    }

    @Test
    void genreNotEqualsToDifferentType() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // SUT
        Genre _sut = genre;

        // Act
        boolean result = _sut.equals("Science Fiction");

        // Assert
        assertFalse(result);
    }

    @Test
    void differentGenresAreNotEqual() {
        // Arrange
        Genre genre1 = new Genre("Science Fiction");
        Genre genre2 = new Genre("ROMANCE");

        // SUT
        Genre _sut = genre1;

        // Act
        boolean result = _sut.equals(genre2);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeShouldBeEqualForSameGenreName() {
        // Arrange
        Genre genre1 = new Genre("Romance ");
        Genre genre2 = new Genre("ROMANCE");

        // SUT
        Genre _sut = genre1;

        // Act
        int result = _sut.hashCode();

        // Assert
        assertEquals(result, genre2.hashCode());
    }

    @Test
    void hashCodeShouldBeDifferentForDifferentGenreNames() {
        // Arrange
        Genre genre1 = new Genre("Science Fiction");
        Genre genre2 = new Genre("ROMANCE");

        // SUT
        Genre _sut = genre1;

        // Act
        int result = _sut.hashCode();

        // Assert
        assertNotEquals(result, genre2.hashCode());
    }

    @Test
    void toStringShouldReturnGenreName() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // SUT
        Genre _sut = genre;

        // Act
        String result = _sut.toString();

        // Assert
        assertEquals("Science Fiction", result);
    }

}

