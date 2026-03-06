package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class GenreFactoryTest {

    @Test
    void factoryShouldCreateGenre() {

        GenreFactory factory = new GenreFactory();

        try (MockedConstruction<Genre> mockedConstruction = Mockito.mockConstruction(Genre.class)) {

            Genre newGenre = factory.createGenre("New Genre");

            Genre constructedGenre = mockedConstruction.constructed().get(0);

            assertNotNull(newGenre);
            assertEquals(1, mockedConstruction.constructed().size());
            assertSame(newGenre, constructedGenre);

        }

    }

    // Test proving that GenreFactory obeys Genre's IllegalArgumentException
    @Test
    void factoryShouldThrowWhenGenreNameIsNull() {

        GenreFactory factory = new GenreFactory();

            assertThrows(IllegalArgumentException.class, () -> factory.createGenre( "   "));

        }

}