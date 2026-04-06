package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.Title;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterNewPublicationControllerTest {

    private UserId _userIdDouble;

    @BeforeEach
    void setUp() {

        _userIdDouble = mock(UserId.class);

    }

    @Test
    void registerPublicationCallsRepoWithCorrectArguments() {
        //arrange
        IPublicationRepo _iPublicationRepo = mock(IPublicationRepo.class);
        PublicationType _typeDouble = mock(PublicationType.class);
        Year _yearDouble = mock(Year.class);
        Title _titleDouble = mock(Title.class);
        Author _authorDouble = mock(Author.class);
        Genre _genreDouble = mock(Genre.class);
        Publication expected = mock(Publication.class);

        when(_iPublicationRepo.addPublication(_titleDouble, _authorDouble, _yearDouble, _typeDouble, _genreDouble))
                .thenReturn(expected);

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(_iPublicationRepo, _userIdDouble);

        //act
        Publication result = controller.registerPublication( _titleDouble, _authorDouble, _yearDouble, _typeDouble, _genreDouble);

        //assert
        assertSame(expected, result);
    }

    @Test
    void registerPublicationThrowsWhenRepoThrows() {
        //arrange

        IPublicationRepo iPublicationRepoDouble = mock(IPublicationRepo.class);
        when(iPublicationRepoDouble.addPublication(any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Duplicate"));

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(iPublicationRepoDouble, _userIdDouble);

        //act and assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerPublication(
                        mock(Title.class),
                        mock(Author.class),
                        mock(Year.class),
                        mock(PublicationType.class),
                        mock(Genre.class)
                )
        );
    }

    @Test
    void constructorThrowsWhenRepoIsNull() {
        //assert
        assertThrows(NullPointerException.class, () ->
                new RegisterNewPublicationController(null, _userIdDouble)
        );
    }

}