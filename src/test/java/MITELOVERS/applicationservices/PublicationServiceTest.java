package MITELOVERS.applicationservices;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicationServiceTest {

    // SUT
    @InjectMocks
    PublicationService _service;

    @Mock
    IPublicationRepo _iPublicationRepoDouble;

    @Mock
    PublicationFactory _publicationFactoryDouble;

    @Mock
    IGenreRepo _iGenreRepoDouble;

    @Mock
    IAuthorRepo _iAuthorRepoDouble;

    String repoExceptionMessage = "PublicationRepo is required";
    String factoryExceptionMessage = "PublicationFactory is required";
    String genreRepoExceptionMessage = "GenreRepo is required";
    String authorRepoExceptionMessage = "AuthorRepo is required";

    // --- Constructor tests ---

    @Test
    void constructorThrowsWhenRepoIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new PublicationService(
                        null,
                        _publicationFactoryDouble,
                        _iGenreRepoDouble,
                        _iAuthorRepoDouble)
        );
        assertEquals(repoExceptionMessage, exception.getMessage());
    }

    @Test
    void constructorThrowsWhenFactoryIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new PublicationService(
                        _iPublicationRepoDouble,
                        null,
                        _iGenreRepoDouble,
                        _iAuthorRepoDouble)
        );
        assertEquals(factoryExceptionMessage, exception.getMessage());
    }

    @Test
    void constructorThrowsWhenGenreRepoIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new PublicationService(
                        _iPublicationRepoDouble,
                        _publicationFactoryDouble,
                        null,
                        _iAuthorRepoDouble)
        );
        assertEquals(genreRepoExceptionMessage, exception.getMessage());
    }

    @Test
    void constructorThrowsWhenAuthorRepoIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new PublicationService(
                        _iPublicationRepoDouble,
                        _publicationFactoryDouble,
                        _iGenreRepoDouble,
                        null)
        );
        assertEquals(authorRepoExceptionMessage, exception.getMessage());
    }

    // --- registerPublication tests ---

    @Test
    void registerPublicationReturnsPublicationWhenSuccessful() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        String synopsis = "synopsis";

        when(_iAuthorRepoDouble.containsOfIdentity(authorIdDouble)).thenReturn(true);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(true);
        when(_publicationFactoryDouble.createPublication(titleDouble, authorIdDouble, yearDouble,
                genreIdDouble, synopsis)).thenReturn(publicationDouble);
        when(publicationDouble.identity()).thenReturn(publicationIdDouble);
        when(_iPublicationRepoDouble.containsOfIdentity(publicationIdDouble)).thenReturn(false);
        when(_iPublicationRepoDouble.save(publicationDouble)).thenReturn(publicationDouble);

        // Act
        Publication result = _service.registerPublication(titleDouble, authorIdDouble, yearDouble,
                genreIdDouble,  synopsis);

        // Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void registerPublicationReturnsExistingPublicationWhenAlreadyExists() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication newPublicationDouble = mock(Publication.class);
        Publication existingPublicationDouble = mock(Publication.class);
        PublicationId newPublicationIdDouble = mock(PublicationId.class);
        PublicationId existingPublicationIdDouble = mock(PublicationId.class);
        String synopsis = "synopsis";

        when(_iAuthorRepoDouble.containsOfIdentity(authorIdDouble)).thenReturn(true);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(true);
        when(_publicationFactoryDouble.createPublication(titleDouble, authorIdDouble, yearDouble,
                genreIdDouble,synopsis)).thenReturn(newPublicationDouble);
        when(newPublicationDouble.identity()).thenReturn(newPublicationIdDouble);

        when(_iPublicationRepoDouble.containsOfIdentity(newPublicationIdDouble)).thenReturn(true);
        when(_iPublicationRepoDouble.ofIdentity(newPublicationIdDouble)).thenReturn(Optional.of(existingPublicationDouble));

        // Act
        Publication result = _service.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble, synopsis);

        // Assert
        assertSame(existingPublicationDouble, result);
    }

    @Test
    void registerPublicationThrowsWhenAuthorDoesNotExist() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        String synopsis = "synopsis";

        when(_iAuthorRepoDouble.containsOfIdentity(authorIdDouble)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                _service.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble, synopsis)
        );
    }

    @Test
    void registerPublicationThrowsWhenGenreDoesNotExist() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        String synopsis = "synopsis";

        when(_iAuthorRepoDouble.containsOfIdentity(authorIdDouble)).thenReturn(true);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                _service.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble, synopsis)
        );
    }

    // --- getPublicationById tests ---

    @Test
    void getPublicationByIdReturnsPublication() {
        // Arrange
        String id = "PUB-001";
        Publication publicationDouble = mock(Publication.class);

        when(_iPublicationRepoDouble.ofIdentity(any(PublicationId.class)))
                .thenReturn(Optional.of(publicationDouble));

        // Act
        Publication result = _service.getPublicationById(id);

        // Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void getPublicationByIdThrowsWhenPublicationDoesNotExist() {
        // Arrange
        String id = "PUB-001";

        when(_iPublicationRepoDouble.ofIdentity(any(PublicationId.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                _service.getPublicationById(id)
        );
        assertEquals("Publication with id 'PUB-001' does not exist", exception.getMessage());
    }

    // --- getAllPublications tests ---

    @Test
    void getAllPublicationsReturnsEmptyListWhenNoPublicationsExist() {
        // Arrange
        when(_iPublicationRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<Publication> result = _service.getAllPublications();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublicationsReturnsListOfPublications() {
        // Arrange
        Publication publication1 = mock(Publication.class);
        Publication publication2 = mock(Publication.class);

        when(_iPublicationRepoDouble.findAll()).thenReturn(List.of(publication1, publication2));

        // Act
        List<Publication> result = _service.getAllPublications();

        // Assert
        assertEquals(2, result.size());
        assertSame(publication1, result.get(0));
        assertSame(publication2, result.get(1));
    }
}