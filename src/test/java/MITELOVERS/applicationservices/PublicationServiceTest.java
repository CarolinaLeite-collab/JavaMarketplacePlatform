package MITELOVERS.applicationservices;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.services.PublicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.NoSuchElementException;

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

    String repoExceptionMessage = "PublicationRepo is required";
    String factoryExceptionMessage = "PublicationFactory is required";
    String publicationAlreadyExistsExceptionMessage = "Publication already exists in the repository";
    String genreIdDoesntExistExceptionMessage = "Genre does not exist in the repository";

    @Test
    void registerPublicationCallsRepoWithCorrectArguments() {

        //Arrange
        Year yearDouble = mock(Year.class);
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);

        when(_publicationFactoryDouble.createPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble))
                .thenReturn(publicationDouble);
        when(_iPublicationRepoDouble.containsOfIdentity(publicationDouble.identity()))
                .thenReturn(false);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble))
                .thenReturn(true);
        when(_iPublicationRepoDouble.save(publicationDouble))
                .thenReturn(publicationDouble);

        //Act
        Publication result = _service.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble);

        //Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void registerPublicationThrowsWhenPublicationAlreadyExists() {

        //Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);

        when(_publicationFactoryDouble.createPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble))
                .thenReturn(publicationDouble);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble))
                .thenReturn(true);
        when(_iPublicationRepoDouble.containsOfIdentity(any()))
                .thenReturn(true);

        //Act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                _service.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble)
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

        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble))
                .thenReturn(false);

        //Act
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                _service.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble)
        );

        //Assert
        assertEquals(genreIdDoesntExistExceptionMessage, exception.getMessage());
    }

    @Test
    void constructorThrowsWhenRepoIsNull() {

        //Act
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                _service = new PublicationService(null, _publicationFactoryDouble, _iGenreRepoDouble)
        );

        //Assert
        assertEquals(repoExceptionMessage, exception.getMessage());
    }

    @Test
    void constructorThrowsWhenFactoryIsNull() {

        //Act
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                _service = new PublicationService(_iPublicationRepoDouble, null, _iGenreRepoDouble)
        );

        //Assert
        assertEquals(factoryExceptionMessage, exception.getMessage());
    }
}
