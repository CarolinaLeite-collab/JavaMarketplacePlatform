package TOPSECRET.domain;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.genre.GenreFactory;
import TOPSECRET.domain.valueobject.GenreId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenreFactoryTest {

    @Test
    void factoryShouldCreateGenre() {

        // Arrange & SUT
        GenreFactory factory = new GenreFactory();

        try (MockedConstruction<Genre> mockedConstruction = mockConstruction(Genre.class)) {

            // Act
            Genre newGenre = factory.createGenre("New Genre");

            Genre constructedGenre = mockedConstruction.constructed().get(0);

            // Assert
            assertNotNull(newGenre);
            assertEquals(1, mockedConstruction.constructed().size());
            assertSame(newGenre, constructedGenre);
        }
    }
    @Test
    void createGenreReconstitutionValidArgsReturnsGenre() {
        // Arrange
        GenreId _genreIdDouble = mock(GenreId.class);
        GenreFactory factory = new GenreFactory();

        try (MockedConstruction<Genre> mocked =
                     mockConstruction(Genre.class,
                             (mock, context) -> {
                                 when(mock.getGenre()).thenReturn("Science Fiction");
                                 when(mock.identity()).thenReturn(_genreIdDouble);
                             })) {

            // Act
            Genre result = factory.createGenre(_genreIdDouble, "Science Fiction"); // SUT

            // Assert
            assertNotNull(result);
            assertEquals("Science Fiction", result.getGenre());
            assertEquals(_genreIdDouble, result.identity());
        }
    }

}