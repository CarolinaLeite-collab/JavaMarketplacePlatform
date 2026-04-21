package MITELOVERS.controller;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.domain.valueobject.UserId;
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
        IPublicationRepo iPublicationRepo = mock(IPublicationRepo.class);
        Year yearDouble = mock(Year.class);
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);
        UserId userIdDouble = mock(UserId.class);

        when(iPublicationRepo.addPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble))
                .thenReturn(publicationDouble);

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(iPublicationRepo, userIdDouble);

        //act
        Publication result = controller.registerPublication( titleDouble, authorIdDouble, yearDouble, genreIdDouble);

        //assert
        assertSame(publicationDouble, result);
    }

    @Test
    void registerPublicationThrowsWhenRepoThrows() {
        //arrange
        UserId userIdDouble = mock(UserId.class);

        IPublicationRepo iPublicationRepoDouble = mock(IPublicationRepo.class);
        when(iPublicationRepoDouble.addPublication(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Duplicate"));

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(iPublicationRepoDouble, userIdDouble);

        //act and assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerPublication(
                        mock(Title.class),
                        mock(AuthorId.class),
                        mock(Year.class),
                        mock(GenreId.class)
                )
        );
    }

    @Test
    void constructorThrowsWhenRepoIsNull() {
        //Arrange
        UserId userIdDouble = mock(UserId.class);

        //assert
        assertThrows(NullPointerException.class, () ->
                new RegisterNewPublicationController(null, userIdDouble)
        );
    }

}
