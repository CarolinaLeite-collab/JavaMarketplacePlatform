package TOPSECRET.controller;

import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.repository.IPublicationRepo;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import TOPSECRET.domain.valueobject.Title;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterNewPublicationControllerTest {

    @Test
    void registerPublicationCallsRepoWithCorrectArguments() {
        //arrange
        IPublicationRepo _iPublicationRepo = mock(IPublicationRepo.class);
        PublicationTypeId _typeIdDouble = mock(PublicationTypeId.class);
        Year _yearDouble = mock(Year.class);
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);
        GenreId _genreIdDouble = mock(GenreId.class);
        Publication expected = mock(Publication.class);
        UserId _userIdDouble = mock(UserId.class);

        when(_iPublicationRepo.addPublication(_titleDouble, _authorIdDouble, _yearDouble, _typeIdDouble, _genreIdDouble))
                .thenReturn(expected);

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(_iPublicationRepo, _userIdDouble);

        //act
        Publication result = controller.registerPublication( _titleDouble, _authorIdDouble, _yearDouble, _typeIdDouble, _genreIdDouble);

        //assert
        assertSame(expected, result);
    }

    @Test
    void registerPublicationThrowsWhenRepoThrows() {
        //arrange
        UserId userIdDouble = mock(UserId.class);

        IPublicationRepo iPublicationRepoDouble = mock(IPublicationRepo.class);
        when(iPublicationRepoDouble.addPublication(any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Duplicate"));

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(iPublicationRepoDouble, userIdDouble);

        //act and assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerPublication(
                        mock(Title.class),
                        mock(AuthorId.class),
                        mock(Year.class),
                        mock(PublicationTypeId.class),
                        mock(GenreId.class)
                )
        );
    }

    @Test
    void constructorThrowsWhenRepoIsNull() {
        //Arrange
        UserId _userIdDouble = mock(UserId.class);

        //assert
        assertThrows(NullPointerException.class, () ->
                new RegisterNewPublicationController(null, _userIdDouble)
        );
    }

}