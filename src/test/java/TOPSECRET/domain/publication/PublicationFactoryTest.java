package TOPSECRET.domain.publication;

import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationFactoryTest {

    @Test
    void createPublication_allFields_returnsPublication() {
        // Arrange
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);
        Year _yearDouble = mock(Year.class);
        PublicationTypeId _publicationTypeIdDouble = mock(PublicationTypeId.class);
        GenreId _genreIdDouble = mock(GenreId.class);
        PublicationFactory factory = new PublicationFactory();

        try (MockedConstruction<Publication> mocked =
                     mockConstruction(Publication.class,
                             (mock, context) -> {
                                 when(mock.getTitle()).thenReturn(_titleDouble);
                                 when(mock.getAuthorId()).thenReturn(_authorIdDouble);
                                 when(mock.getReleaseYear()).thenReturn(_yearDouble);
                                 when(mock.getPublicationTypeId()).thenReturn(_publicationTypeIdDouble);
                                 when(mock.getGenreId()).thenReturn(_genreIdDouble);
                             })) {

            // Act
            Publication result = factory.createPublication(_titleDouble, _authorIdDouble,
                    _yearDouble, _publicationTypeIdDouble, _genreIdDouble); // SUT

            // Assert
            assertNotNull(result);
            assertEquals(_titleDouble, result.getTitle());
            assertEquals(_authorIdDouble, result.getAuthorId());
            assertEquals(_yearDouble, result.getReleaseYear());
            assertEquals(_publicationTypeIdDouble, result.getPublicationTypeId());
            assertEquals(_genreIdDouble, result.getGenreId());
        }
    }
    @Test
    void createPublicationReconstitution_allFields_returnsPublication() {
        // Arrange
        PublicationId _publicationIdDouble = mock(PublicationId.class);
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);
        Year _yearDouble = mock(Year.class);
        PublicationTypeId _publicationTypeIdDouble = mock(PublicationTypeId.class);
        GenreId _genreIdDouble = mock(GenreId.class);
        PublicationFactory factory = new PublicationFactory();

        try (MockedConstruction<Publication> mocked =
                     mockConstruction(Publication.class,
                             (mock, context) -> {
                                 when(mock.getPublicationId()).thenReturn(_publicationIdDouble);
                                 when(mock.getTitle()).thenReturn(_titleDouble);
                                 when(mock.getAuthorId()).thenReturn(_authorIdDouble);
                                 when(mock.getReleaseYear()).thenReturn(_yearDouble);
                                 when(mock.getPublicationTypeId()).thenReturn(_publicationTypeIdDouble);
                                 when(mock.getGenreId()).thenReturn(_genreIdDouble);
                             })) {

            // Act
            Publication result = factory.createPublication(_publicationIdDouble, _titleDouble,
                    _authorIdDouble, _yearDouble, _publicationTypeIdDouble, _genreIdDouble); // SUT

            // Assert
            assertNotNull(result);
            assertEquals(_publicationIdDouble, result.getPublicationId());
            assertEquals(_titleDouble, result.getTitle());
            assertEquals(_authorIdDouble, result.getAuthorId());
            assertEquals(_yearDouble, result.getReleaseYear());
            assertEquals(_publicationTypeIdDouble, result.getPublicationTypeId());
            assertEquals(_genreIdDouble, result.getGenreId());
        }
    }

}