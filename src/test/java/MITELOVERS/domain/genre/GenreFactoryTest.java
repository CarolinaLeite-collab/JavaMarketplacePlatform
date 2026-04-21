package MITELOVERS.domain.genre;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;

class GenreFactoryTest {

    @Test
    void factoryShouldCreateGenre() {



        try (MockedConstruction<Genre> mockedConstruction = mockConstruction(Genre.class)) {

            // Arrange & SUT
            GenreFactory factory = new GenreFactory();
            // Act
            Genre newGenre = factory.createGenre("New Genre");

            Genre constructedGenre = mockedConstruction.constructed().get(0);

            // Assert
            assertNotNull(newGenre);
            assertEquals(1, mockedConstruction.constructed().size());
            assertSame(newGenre, constructedGenre);
        }
    }
}
