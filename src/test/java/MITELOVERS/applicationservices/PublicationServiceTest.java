package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.dto.response.PublicationResponseDTO;
import MITELOVERS.mapper.PublicationResponseDTOMapper;
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

    //SUT
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

    @Mock
    PublicationResponseDTOMapper _publicationResponseDTOMapper;

    String repoExceptionMessage = "PublicationRepo is required";
    String factoryExceptionMessage = "PublicationFactory is required";
    String genreRepoExceptionMessage = "GenreRepo is required";
    String authorRepoExceptionMessage = "AuthorRepo is required";
    String mapperExceptionMessage = "PublicationDTOAssembler is required";
    String genreIdDoesntExistExceptionMessage = "Genre does not exist in the repository";
    String authorIdDoesntExistExceptionMessage = "Author does not exist in the repository";

    @Test
    void registerPublicationCallsRepoWithCorrectArguments() {
        // Arrange
        Year yearDouble = mock(Year.class);
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);

        when(_publicationFactoryDouble.createPublication(
                titleDouble,
                authorIdDouble,
                yearDouble,
                genreIdDouble
        )).thenReturn(publicationDouble);

        when(publicationDouble.identity())
                .thenReturn(publicationIdDouble);

        when(_iPublicationRepoDouble.containsOfIdentity(publicationIdDouble))
                .thenReturn(false);

        when(_iPublicationRepoDouble.save(publicationDouble))
                .thenReturn(publicationDouble);

        // Act
        Publication result = _service.registerPublication(
                titleDouble,
                authorIdDouble,
                yearDouble,
                genreIdDouble
        );

        // Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void constructorThrowsWhenRepoIsNull() {

        //Act
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                _service = new PublicationService(
                        null,
                        _publicationFactoryDouble,
                        _iGenreRepoDouble,
                        _iAuthorRepoDouble,
                        _publicationResponseDTOMapper)
        );

        //Assert
        assertEquals(repoExceptionMessage, exception.getMessage());
    }

    @Test
    void constructorThrowsWhenFactoryIsNull() {

        //Act
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                _service = new PublicationService(
                        _iPublicationRepoDouble,
                        null,
                        _iGenreRepoDouble,
                        _iAuthorRepoDouble,
                        _publicationResponseDTOMapper)
        );

        //Assert
        assertEquals(factoryExceptionMessage, exception.getMessage());
    }

    @Test
    void constructorThrowsWhenGenreRepoIsNull() {

        //Act
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                _service = new PublicationService(
                        _iPublicationRepoDouble,
                        _publicationFactoryDouble,
                        null,
                        _iAuthorRepoDouble,
                        _publicationResponseDTOMapper)
        );

        //Assert
        assertEquals(genreRepoExceptionMessage, exception.getMessage());
    }

    @Test
    void constructorThrowsWhenAuthorRepoIsNull() {

        //Act
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                _service = new PublicationService(
                        _iPublicationRepoDouble,
                        _publicationFactoryDouble,
                        _iGenreRepoDouble,
                        null,
                        _publicationResponseDTOMapper)
        );

        //Assert
        assertEquals(authorRepoExceptionMessage, exception.getMessage());
    }

    @Test
    void constructorThrowsWhenMapperIsNull() {

        //Act
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                _service = new PublicationService(
                        _iPublicationRepoDouble,
                        _publicationFactoryDouble,
                        _iGenreRepoDouble,
                        _iAuthorRepoDouble,
                        null)
        );

        //Assert
        assertEquals(mapperExceptionMessage, exception.getMessage());
    }

    @Test
    void registerPublicationReturnsExistingPublicationWhenPublicationAlreadyExists() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);

        Publication newPublicationDouble = mock(Publication.class);
        Publication existingPublicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);

        when(_publicationFactoryDouble.createPublication(
                titleDouble,
                authorIdDouble,
                yearDouble,
                genreIdDouble
        )).thenReturn(newPublicationDouble);

        when(newPublicationDouble.identity())
                .thenReturn(publicationIdDouble);

        when(_iPublicationRepoDouble.containsOfIdentity(publicationIdDouble))
                .thenReturn(true);

        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble))
                .thenReturn(Optional.of(existingPublicationDouble));

        // Act
        Publication result = _service.registerPublication(
                titleDouble,
                authorIdDouble,
                yearDouble,
                genreIdDouble
        );

        // Assert
        assertSame(existingPublicationDouble, result);
    }

    @Test
    void getPublicationByIdReturnsPublicationResponseDTO() {
        //Arrange
        String id = "PUB-001";

        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        Genre genreDouble = mock(Genre.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        PublicationResponseDTO responseDTODouble = mock(PublicationResponseDTO.class);

        when(_iPublicationRepoDouble.ofIdentity(any(PublicationId.class)))
                .thenReturn(Optional.of(publicationDouble));

        when(publicationDouble.getAuthorId())
                .thenReturn(authorIdDouble);

        when(publicationDouble.getGenreId())
                .thenReturn(genreIdDouble);

        when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
                .thenReturn(Optional.of(authorDouble));

        when(_iGenreRepoDouble.ofIdentity(genreIdDouble))
                .thenReturn(Optional.of(genreDouble));

        when(_publicationResponseDTOMapper.toResponseDTO(
                publicationDouble,
                authorDouble,
                genreDouble
        )).thenReturn(responseDTODouble);

        // Act
        PublicationResponseDTO result = _service.getPublicationById(id);

        // Assert
        assertSame(responseDTODouble, result);
    }

    @Test
    void getPublicationByIdThrowsWhenPublicationDoesNotExist() {
        //Arrange
        String id = "PUB-001";

        when(_iPublicationRepoDouble.ofIdentity(any(PublicationId.class)))
                .thenReturn(Optional.empty());

        //Act
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> _service.getPublicationById(id)
        );

        //Assert
        assertEquals("Publication with id 'PUB-001' does not exist", exception.getMessage());
    }

    @Test
    void getPublicationByIdThrowsWhenAuthorDoesNotExist() {
        // Arrange
        String id = "PUB-001";

        Publication publicationDouble = mock(Publication.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_iPublicationRepoDouble.ofIdentity(any(PublicationId.class)))
                .thenReturn(Optional.of(publicationDouble));

        when(publicationDouble.getAuthorId())
                .thenReturn(authorIdDouble);

        when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
                .thenReturn(Optional.empty());

        // Act
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> _service.getPublicationById(id)
        );

        // Assert
        assertEquals(authorIdDoesntExistExceptionMessage, exception.getMessage());
    }

    @Test
    void getPublicationByIdThrowsWhenGenreDoesNotExist() {
        // Arrange
        String id = "PUB-001";

        Publication publicationDouble = mock(Publication.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Author authorDouble = mock(Author.class);

        when(_iPublicationRepoDouble.ofIdentity(any(PublicationId.class)))
                .thenReturn(Optional.of(publicationDouble));

        when(publicationDouble.getAuthorId())
                .thenReturn(authorIdDouble);

        when(publicationDouble.getGenreId())
                .thenReturn(genreIdDouble);

        when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
                .thenReturn(Optional.of(authorDouble));

        when(_iGenreRepoDouble.ofIdentity(genreIdDouble))
                .thenReturn(Optional.empty());

        // Act
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> _service.getPublicationById(id)
        );

        // Assert
        assertEquals(genreIdDoesntExistExceptionMessage, exception.getMessage());
    }

    @Test
    void getAllPublicationsReturnsEmptyListWhenNoPublicationsExist() {
        //Arrange
        when(_iPublicationRepoDouble.findAll()).thenReturn(List.of());

        //Act
        List<PublicationResponseDTO> result = _service.getAllPublications();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublicationsThrowsWhenAuthorDoesNotExist() {
        // Arrange
        Publication publication = mock(Publication.class);
        AuthorId authorId = mock(AuthorId.class);

        when(_iPublicationRepoDouble.findAll()).thenReturn(List.of(publication));

        when(publication.getAuthorId()).thenReturn(authorId);

        when(_iAuthorRepoDouble.ofIdentity(authorId)).thenReturn(Optional.empty());

        //Act
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> _service.getAllPublications()
        );

        //Assert
        assertEquals("Author does not exist in the repository", exception.getMessage());
    }

    @Test
    void getAllPublicationsThrowsWhenGenreDoesNotExist() {
        // Arrange
        Publication publication = mock(Publication.class);
        AuthorId authorId = mock(AuthorId.class);
        GenreId genreId = mock(GenreId.class);
        Author author = mock(Author.class);

        when(_iPublicationRepoDouble.findAll())
                .thenReturn(List.of(publication));

        when(publication.getAuthorId())
                .thenReturn(authorId);

        when(publication.getGenreId())
                .thenReturn(genreId);

        when(_iAuthorRepoDouble.ofIdentity(authorId))
                .thenReturn(Optional.of(author));

        when(_iGenreRepoDouble.ofIdentity(genreId))
                .thenReturn(Optional.empty());

        // Act
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> _service.getAllPublications()
        );

        // Assert
        assertEquals("Genre does not exist in the repository", exception.getMessage());
    }

    @Test
    void getAllPublicationsReturnsListOfPublicationResponseDTOs() {
        // Arrange
        Publication publication1 = mock(Publication.class);
        Publication publication2 = mock(Publication.class);

        AuthorId authorId1 = mock(AuthorId.class);
        AuthorId authorId2 = mock(AuthorId.class);

        GenreId genreId1 = mock(GenreId.class);
        GenreId genreId2 = mock(GenreId.class);

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);

        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);

        PublicationResponseDTO responseDTO1 = mock(PublicationResponseDTO.class);
        PublicationResponseDTO responseDTO2 = mock(PublicationResponseDTO.class);

        when(_iPublicationRepoDouble.findAll())
                .thenReturn(List.of(publication1, publication2));

        when(publication1.getAuthorId()).thenReturn(authorId1);
        when(publication1.getGenreId()).thenReturn(genreId1);

        when(publication2.getAuthorId()).thenReturn(authorId2);
        when(publication2.getGenreId()).thenReturn(genreId2);

        when(_iAuthorRepoDouble.ofIdentity(authorId1))
                .thenReturn(Optional.of(author1));
        when(_iAuthorRepoDouble.ofIdentity(authorId2))
                .thenReturn(Optional.of(author2));

        when(_iGenreRepoDouble.ofIdentity(genreId1))
                .thenReturn(Optional.of(genre1));
        when(_iGenreRepoDouble.ofIdentity(genreId2))
                .thenReturn(Optional.of(genre2));

        when(_publicationResponseDTOMapper.toResponseDTO(publication1, author1, genre1))
                .thenReturn(responseDTO1);
        when(_publicationResponseDTOMapper.toResponseDTO(publication2, author2, genre2))
                .thenReturn(responseDTO2);

        // Act
        List<PublicationResponseDTO> result = _service.getAllPublications();

        // Assert
        assertEquals(2, result.size());
        assertSame(responseDTO1, result.get(0));
        assertSame(responseDTO2, result.get(1));
    }
}
