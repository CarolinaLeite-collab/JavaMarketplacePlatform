package MITELOVERS.domain.publication;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PublicationFactoryTest {

    @Test
    void createPublication_allFields_returnsPublication() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        PublicationFactory factory = new PublicationFactory();

        try (MockedConstruction<Publication> mocked =
                     mockConstruction(Publication.class,
                             (mock, context) -> {
                                 when(mock.getTitle()).thenReturn(titleDouble);
                                 when(mock.getAuthorId()).thenReturn(authorIdDouble);
                                 when(mock.getReleaseYear()).thenReturn(yearDouble);
                                 when(mock.getGenreId()).thenReturn(genreIdDouble);
                             })) {

            // Act
            Publication result = factory.createPublication(titleDouble, authorIdDouble,
                    yearDouble, genreIdDouble); // SUT

            // Assert
            assertNotNull(result);
            assertEquals(titleDouble, result.getTitle());
            assertEquals(authorIdDouble, result.getAuthorId());
            assertEquals(yearDouble, result.getReleaseYear());
            assertEquals(genreIdDouble, result.getGenreId());
        }
    }

}
