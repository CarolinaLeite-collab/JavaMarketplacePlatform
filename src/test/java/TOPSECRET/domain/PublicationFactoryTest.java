package TOPSECRET.domain;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.PublicationId;
import TOPSECRET.domain.valueobject.Title;
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
        Title _titleDouble = mock(Title.class);
        Author _authorDouble = mock(Author.class);
        Year _yearDouble = mock(Year.class);
        PublicationType _publicationTypeDouble = mock(PublicationType.class);
        Genre _genreDouble = mock(Genre.class);
        PublicationFactory factory = new PublicationFactory();

        try (MockedConstruction<Publication> mocked =
                     mockConstruction(Publication.class,
                             (mock, context) -> {
                                 when(mock.getTitle()).thenReturn(_titleDouble);
                                 when(mock.getAuthor()).thenReturn(_authorDouble);
                                 when(mock.getReleaseYear()).thenReturn(_yearDouble);
                                 when(mock.getPublicationType()).thenReturn(_publicationTypeDouble);
                                 when(mock.getGenre()).thenReturn(_genreDouble);
                             })) {

            // Act
            Publication result = factory.createPublication(_titleDouble, _authorDouble,
                    _yearDouble, _publicationTypeDouble, _genreDouble); // SUT

            // Assert
            assertNotNull(result);
            assertEquals(_titleDouble, result.getTitle());
            assertEquals(_authorDouble, result.getAuthor());
            assertEquals(_yearDouble, result.getReleaseYear());
            assertEquals(_publicationTypeDouble, result.getPublicationType());
            assertEquals(_genreDouble, result.getGenre());
        }
    }
    @Test
    void createPublicationReconstitution_allFields_returnsPublication() {
        // Arrange
        PublicationId _publicationIdDouble = mock(PublicationId.class);
        Title _titleDouble = mock(Title.class);
        Author _authorDouble = mock(Author.class);
        Year _yearDouble = mock(Year.class);
        PublicationType _publicationTypeDouble = mock(PublicationType.class);
        Genre _genreDouble = mock(Genre.class);
        PublicationFactory factory = new PublicationFactory();

        try (MockedConstruction<Publication> mocked =
                     mockConstruction(Publication.class,
                             (mock, context) -> {
                                 when(mock.getPublicationId()).thenReturn(_publicationIdDouble);
                                 when(mock.getTitle()).thenReturn(_titleDouble);
                                 when(mock.getAuthor()).thenReturn(_authorDouble);
                                 when(mock.getReleaseYear()).thenReturn(_yearDouble);
                                 when(mock.getPublicationType()).thenReturn(_publicationTypeDouble);
                                 when(mock.getGenre()).thenReturn(_genreDouble);
                             })) {

            // Act
            Publication result = factory.createPublication(_publicationIdDouble, _titleDouble,
                    _authorDouble, _yearDouble, _publicationTypeDouble, _genreDouble); // SUT

            // Assert
            assertNotNull(result);
            assertEquals(_publicationIdDouble, result.getPublicationId());
            assertEquals(_titleDouble, result.getTitle());
            assertEquals(_authorDouble, result.getAuthor());
            assertEquals(_yearDouble, result.getReleaseYear());
            assertEquals(_publicationTypeDouble, result.getPublicationType());
            assertEquals(_genreDouble, result.getGenre());
        }
    }

}