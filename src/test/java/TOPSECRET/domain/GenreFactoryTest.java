package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class GenreFactoryTest {

    @Test
    void factoryShouldCreateGenre() {

        // Arrange & SUT
        GenreFactory factory = new GenreFactory();

        try (MockedConstruction<Genre> mockedConstruction = Mockito.mockConstruction(Genre.class)) {

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