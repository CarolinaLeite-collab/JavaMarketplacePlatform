package MITELOVERS.controller;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
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
        //Arrange
        IPublicationRepo iPublicationRepo = mock(IPublicationRepo.class);
        Year yearDouble = mock(Year.class);
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);
        UserId userIdDouble = mock(UserId.class);
        PublicationFactory publicationFactory = mock(PublicationFactory.class);

        when(publicationFactory.createPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble))
                .thenReturn(publicationDouble);
        when(iPublicationRepo.containsOfIdentity(publicationDouble.identity()))
                .thenReturn(false);
        when(iPublicationRepo.save(publicationDouble))
                .thenReturn(publicationDouble);


        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(iPublicationRepo, publicationFactory, userIdDouble);

        //Act
        Publication result = controller.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble);

        //Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void registerPublicationThrowsWhenRepoThrows() {
        //Arrange
        UserId userIdDouble = mock(UserId.class);
        PublicationFactory publicationFactory = mock(PublicationFactory.class);
        IPublicationRepo iPublicationRepoDouble = mock(IPublicationRepo.class);

        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);

        when(publicationFactory.createPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble))
                .thenReturn(publicationDouble);
        when(iPublicationRepoDouble.containsOfIdentity(any()))
                .thenReturn(false);
        when(iPublicationRepoDouble.save(publicationDouble))
                .thenThrow(new IllegalArgumentException("Duplicate"));

        //SUT
        RegisterNewPublicationController controller =
                new RegisterNewPublicationController(iPublicationRepoDouble, publicationFactory, userIdDouble);

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerPublication(titleDouble, authorIdDouble, yearDouble, genreIdDouble)
        );
    }

    @Test
    void constructorThrowsWhenRepoIsNull() {
        //Arrange
        UserId userIdDouble = mock(UserId.class);
        PublicationFactory publicationFactory = mock(PublicationFactory.class);

        //assert
        assertThrows(NullPointerException.class, () ->
                new RegisterNewPublicationController(null, publicationFactory, userIdDouble)
        );
    }

}
