package MITELOVERS.controller;

import MITELOVERS.controllers.cli.RegisterNewPublicationController;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class RegisterNewPublicationControllerTest {

    //SUT
    @InjectMocks
    RegisterNewPublicationController _controller;

    @Mock
    IPublicationRepo _iPublicationRepoDouble;

    @Mock
    PublicationFactory _publicationFactoryDouble;

    @Mock
    IAuthorRepo _iAuthorRepoDouble;

    @Mock
    IGenreRepo _iGenreRepoDouble;


    @BeforeEach
    void setUp() throws InstantiationException {
        MockitoAnnotations.openMocks(this);
    }

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
        when(_iPublicationRepoDouble.save(publicationDouble))
                .thenReturn(publicationDouble);

        //Act
        Publication result = _controller.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble);

        //Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void registerPublicationThrowsWhenRepoThrows() {
        //Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);

        when(_publicationFactoryDouble.createPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble))
                .thenReturn(publicationDouble);
        when(_iPublicationRepoDouble.containsOfIdentity(any()))
                .thenReturn(false);
        when(_iPublicationRepoDouble.save(publicationDouble))
                .thenThrow(new IllegalArgumentException("Duplicate"));

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                _controller.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble)
        );
    }

    @Test
    void constructorThrowsWhenRepoIsNull() {
        //Arrange
        UserId userIdDouble = mock(UserId.class);
        PublicationFactory publicationFactoryDouble = mock(PublicationFactory.class);

        //assert
        assertThrows(NullPointerException.class, () ->
                _controller = new RegisterNewPublicationController(null, publicationFactoryDouble, _iAuthorRepoDouble, _iGenreRepoDouble)
        );
    }

    @Test
    void shouldReturnAllAuthorIds(){
        //arrange
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_iAuthorRepoDouble.findAllKeys()).thenReturn(List.of(authorIdDouble));

        //act
        Iterable<AuthorId> result = _controller.getAuthorsId();

        List<AuthorId> ids = new ArrayList<>();
        for (AuthorId authorId : result) {
            ids.add(authorId);
        }

        //assert
        assertEquals(authorIdDouble, ids.get(0));
    }

    @Test
    void shouldReturnAllGenreIds(){
        //arrange
        GenreId genreIdDouble = mock(GenreId.class);

        when(_iGenreRepoDouble.findAllKeys()).thenReturn(List.of(genreIdDouble));

        //act
        Iterable<GenreId> result = _controller.getGenresId();

        List<GenreId> ids = new ArrayList<>();
        for (GenreId genreId : result) {
            ids.add(genreId);
        }

        //assert
        assertEquals(genreIdDouble, ids.get(0));
    }

}
