package TOPSECRET.domain.genre;

import TOPSECRET.domain.valueobject.GenreId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GenreTest {

    @Test
    void constructorShouldBuildGenre() {

        // Act
        Genre genre = new Genre("Science Fiction");

        // Assert
        assertNotNull(genre);
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
    void constructorCreationGeneratesGenreId() {
        // Arrange
        String name = "Science Fiction";

        // Act
        Genre genre = new Genre(name);

        // Assert
        assertNotNull(genre.identity());
    }

    @Test
    void constructorReconstitutionValidArgsCreatesGenre() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        String name = "Science Fiction";

        // Act
        Genre genre = new Genre(genreIdDouble, name); // SUT

        // Assert
        assertNotNull(genre);
    }

    @Test
    void constructorReconstitutionNullGenreIdThrowsNullPointerException() {
        // Arrange
        String name = "Science Fiction";

        // Act
        Exception exception = assertThrows(NullPointerException.class, () ->
                new Genre(null, name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorReconstitutionNullNameThrowsIllegalArgumentException() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Genre(genreIdDouble, null));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorReconstitutionRestoresGenreId() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        String name = "Science Fiction";

        // Act
        Genre genre = new Genre(genreIdDouble, name);

        // Assert
        assertSame(genreIdDouble, genre.identity());
    }
    @Test
    void identityReturnsNonNullGenreId() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // Act
        GenreId id = genre.identity();

        // Assert
        assertNotNull(id);
    }

    @Test
    void sameAsSameInstanceReturnsTrue() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // Act
        boolean result = genre.sameAs(genre);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsSameNameReturnsTrue() {
        // Arrange
        Genre genre1 = new Genre("Science Fiction");

        // Act
        Genre genre2 = new Genre("Science FictIon");

        // Assert
        assertTrue(genre1.sameAs(genre2));
    }

    @Test
    void sameAsDifferentNameReturnsFalse() {
        // Arrange
        Genre genre1 = new Genre("Science Fiction");

        // Act
        Genre genre2 = new Genre("Romance");

        // Assert
        assertFalse(genre1.sameAs(genre2));
    }

    @Test
    void sameAsNullReturnsFalse() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // Act
        boolean result = genre.sameAs(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        // Arrange
        Genre genre = new Genre("Science Fiction");

        // Act
        boolean result = genre.sameAs("Science Fiction");

        // Assert
        assertFalse(result);
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