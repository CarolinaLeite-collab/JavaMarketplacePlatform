package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    @Test
    void constructorShouldBuildGenre() {

        // Act
        Genre genre = new Genre("Science Fiction");

        // Assert
        assertEquals("Science Fiction", genre.getGenre());
    }

    @Test
    void constructorShouldTrimGenreName() {

        // Act
        Genre genre = new Genre(" Science Fiction  ");

        // Assert
        assertEquals("Science Fiction", genre.getGenre());
    }

    @Test
    void constructorShouldThrowExceptionWhenGenreNameIsBlank() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Genre("  "));
    }

    @Test
    void constructorShouldThrowWhenGenreNameIsNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Genre(null));
    }

    @Test
    void genreNotEqualsToNull() {

        // Arrange
        Genre genre = new Genre("Science Fiction");

        // Assert
        assertNotEquals(genre, null);
    }

    @Test
    void genreWithSameName() {

        // Arrange
        Genre genre = new Genre("Romance ");
        Genre genre2 = new Genre("ROMANCE");

        // Assert
        assertEquals(genre, genre2);
    }

    @Test
    void genreEqualsItself() {

        // Arrange
        Genre g = new Genre("Science Fiction");

        // Assert
        assertEquals(g, g);
    }

    @Test
    void genreNotEqualsToDifferentType() {

        // Arrange
        Genre genre = new Genre("Science Fiction");

        // Assert
        assertNotEquals(genre, "Science Fiction");
    }

    @Test
    void differentGenresAreNotEqual() {

        // Arrange
        Genre genre1 = new Genre("Science Fiction");
        Genre genre2 = new Genre("ROMANCE");

        // Assert
        assertNotEquals(genre1, genre2);
    }

    @Test
    void hashCodeShouldBeEqualForSameGenreName() {

        // Arrange
        Genre genre1 = new Genre("Romance ");
        Genre genre2 = new Genre("ROMANCE");

        // Act & Assert
        assertEquals(genre1.hashCode(), genre2.hashCode());
    }

    @Test
    void hashCodeShouldBeDifferentForDifferentGenreNames() {

        // Arrange
        Genre genre1 = new Genre("Science Fiction");
        Genre genre2 = new Genre("ROMANCE");

        // Act & Assert
        assertNotEquals(genre1.hashCode(), genre2.hashCode());
    }

    @Test
    void toStringShouldReturnGenreName() {

        // Arrange
        Genre genre = new Genre("Science Fiction");

        // Act
        String result = genre.toString();

        // Assert
        assertEquals("Science Fiction",result);
    }

}