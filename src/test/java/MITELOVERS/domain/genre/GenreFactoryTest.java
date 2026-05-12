package MITELOVERS.domain.genre;

import MITELOVERS.domain.valueobject.GenreId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;

class GenreFactoryTest {

    @Test
    void factoryShouldCreateGenre() {
        // Arrange

        // SUT
        GenreFactory factory = new GenreFactory();

        // Act
        try (MockedConstruction<Genre> mockedConstruction = mockConstruction(Genre.class)) {
            Genre newGenre = factory.createGenre("New Genre");
            Genre constructedGenre = mockedConstruction.constructed().get(0);

            // Assert
            assertNotNull(newGenre);
            assertEquals(1, mockedConstruction.constructed().size());
            assertSame(newGenre, constructedGenre);
        }
    }

    @Test
    void factoryShouldCreateGenreWithId() {
        // Arrange
        GenreId genreId = new GenreId("Mystery");

        // SUT
        GenreFactory factory = new GenreFactory();

        // Act
        try (MockedConstruction<Genre> mockedConstruction = mockConstruction(Genre.class,
                (mock, context) -> {
                    assertEquals(2, context.arguments().size());
                    assertEquals(genreId, context.arguments().get(0));
                    assertEquals("Mystery", context.arguments().get(1));
                })) {
            Genre newGenre = factory.createGenre(genreId, "Mystery");
            Genre constructedGenre = mockedConstruction.constructed().get(0);

            // Assert
            assertNotNull(newGenre);
            assertEquals(1, mockedConstruction.constructed().size());
            assertSame(newGenre, constructedGenre);
        }
    }

    @Test
    void factoryShouldThrowWhenGenreNameIsNull() {
        // Arrange
        GenreFactory factory = new GenreFactory();

        // Act
        Exception result = assertThrows(IllegalArgumentException.class, () -> factory.createGenre((String) null));

        // Assert
        assertNotNull(result);
    }

    @Test
    void factoryShouldThrowWhenGenreNameIsBlank() {
        // Arrange
        GenreFactory factory = new GenreFactory();

        // Act
        Exception result = assertThrows(IllegalArgumentException.class, () -> factory.createGenre("  "));

        // Assert
        assertNotNull(result);
    }

    @Test
    void factoryShouldThrowWhenGenreIdIsNull() {
        // Arrange
        GenreFactory factory = new GenreFactory();

        // Act
        Exception result = assertThrows(NullPointerException.class, () -> factory.createGenre(null, "Mystery"));

        // Assert
        assertNotNull(result);
    }

    @Test
    void factoryShouldThrowWhenGenreNameIsNullWithId() {
        // Arrange
        GenreId genreId = new GenreId("Mystery");
        GenreFactory factory = new GenreFactory();

        // Act
        Exception result = assertThrows(NullPointerException.class, () -> factory.createGenre(genreId, null));

        // Assert
        assertNotNull(result);
    }
}
