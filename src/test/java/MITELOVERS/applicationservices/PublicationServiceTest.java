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
import MITELOVERS.dto.PublicationResponseDTO;
import MITELOVERS.mapper.PublicationResponseDTOMapper;
import MITELOVERS.services.PublicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
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
    String publicationAlreadyExistsExceptionMessage = "Publication already exists in the repository";
    String genreIdDoesntExistExceptionMessage = "Genre does not exist in the repository";
    String authorIdDoesntExistExceptionMessage = "Author does not exist in the repository";

    @Test
    void registerPublicationCallsRepoWithCorrectArguments() {

        //Arrange
        Year yearDouble = mock(Year.class);
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        Author authorDouble = mock(Author.class);
        Genre genreDouble = mock(Genre.class);
        PublicationResponseDTO responseDTODouble = mock(PublicationResponseDTO.class);

        when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
                .thenReturn(Optional.of(authorDouble));

        when(_iGenreRepoDouble.ofIdentity(genreIdDouble))
                .thenReturn(Optional.of(genreDouble));

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

        when(_publicationResponseDTOMapper.toResponseDTO(
                publicationDouble,
                authorDouble,
                genreDouble
        )).thenReturn(responseDTODouble);

        //Act
        PublicationResponseDTO result = _service.registerPublication(
                titleDouble,
                authorIdDouble,
                yearDouble,
                genreIdDouble
        );

        //Assert
        assertSame(responseDTODouble, result);
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
    void registerPublicationThrowsWhenPublicationAlreadyExists() {
        //Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        Genre genreDouble = mock(Genre.class);
        Author authorDouble = mock(Author.class);

        when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
                .thenReturn(Optional.of(authorDouble));

        when(_iGenreRepoDouble.ofIdentity(genreIdDouble))
                .thenReturn(Optional.of(genreDouble));

        when(_publicationFactoryDouble.createPublication(
                titleDouble,
                authorIdDouble,
                yearDouble,
                genreIdDouble
        )).thenReturn(publicationDouble);

        when(publicationDouble.identity())
                .thenReturn(publicationIdDouble);

        when(_iPublicationRepoDouble.containsOfIdentity(publicationIdDouble))
                .thenReturn(true);

        //Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _service.registerPublication(
                        titleDouble,
                        authorIdDouble,
                        yearDouble,
                        genreIdDouble
                )
        );

        //Assert
        assertEquals(publicationAlreadyExistsExceptionMessage, exception.getMessage());
    }

    @Test
    void registerPublicationThrowsWhenGenreIdDoesntExists() {

        //Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Author authorDouble = mock(Author.class);

        when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
                .thenReturn(Optional.of(authorDouble));

        when(_iGenreRepoDouble.ofIdentity(genreIdDouble))
                .thenReturn(Optional.empty());

        //Act
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                _service.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble)
        );

        //Assert
        assertEquals(genreIdDoesntExistExceptionMessage, exception.getMessage());
    }

    @Test
    void registerPublicationThrowsWhenAuthorIdDoesntExists() {

        //Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(_iAuthorRepoDouble.ofIdentity(authorIdDouble))
                .thenReturn(Optional.empty());

        //Act
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                _service.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble)
        );

        //Assert
        assertEquals(authorIdDoesntExistExceptionMessage, exception.getMessage());
    }
}
