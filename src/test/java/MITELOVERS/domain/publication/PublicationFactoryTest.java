package MITELOVERS.domain.publication;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PublicationFactoryTest {

    @Test
    void createPublicationWithoutIdreturnsPublication() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        String _synopsis = "Synopsis";

        //SUT
        PublicationFactory factory = new PublicationFactory();

        try (MockedConstruction<Publication> mocked =
                     mockConstruction(Publication.class,
                             (mock, context) -> {
                                 when(mock.getTitle()).thenReturn(titleDouble);
                                 when(mock.getAuthorId()).thenReturn(authorIdDouble);
                                 when(mock.getReleaseYear()).thenReturn(yearDouble);
                                 when(mock.getGenreId()).thenReturn(genreIdDouble);
                                 when(mock.getSynopsis()).thenReturn(_synopsis);
                             })) {

            // Act
            Publication result = factory.createPublication(titleDouble, authorIdDouble,
                    yearDouble, genreIdDouble, _synopsis); // SUT

            // Assert
            assertNotNull(result);
            assertEquals(titleDouble, result.getTitle());
            assertEquals(authorIdDouble, result.getAuthorId());
            assertEquals(yearDouble, result.getReleaseYear());
            assertEquals(genreIdDouble, result.getGenreId());
            assertEquals(_synopsis, result.getSynopsis());
        }
    }

    @Test
    void createPublicationWithIdReturnsPublication() {
        //arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        String _synopsis = "Synopsis";

        PublicationFactory factory = new PublicationFactory();

        try (MockedConstruction<Publication> mocked =
                     mockConstruction(Publication.class,
                             (mock, context) -> {
                                 when(mock.identity()).thenReturn(publicationIdDouble);
                                 when(mock.getTitle()).thenReturn(titleDouble);
                                 when(mock.getAuthorId()).thenReturn(authorIdDouble);
                                 when(mock.getReleaseYear()).thenReturn(yearDouble);
                                 when(mock.getGenreId()).thenReturn(genreIdDouble);
                                 when(mock.getSynopsis()).thenReturn(_synopsis);
                     })) {

            // Act
            Publication result = factory.createPublication(publicationIdDouble, titleDouble, authorIdDouble,
                    yearDouble, genreIdDouble, _synopsis); // SUT

            // Assert
            assertNotNull(result);
            assertEquals(publicationIdDouble, result.identity());
            assertEquals(titleDouble, result.getTitle());
            assertEquals(authorIdDouble, result.getAuthorId());
            assertEquals(yearDouble, result.getReleaseYear());
            assertEquals(genreIdDouble, result.getGenreId());
            assertEquals(_synopsis, result.getSynopsis());
        }
    }

}
