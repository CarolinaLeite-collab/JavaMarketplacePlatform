package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Title;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterNewPublicationControllerTest {

    @Test
    void registerPublicationCallsRepoWithCorrectArguments() {
        //arrange
        PublicationRepo _publicationRepo = mock(PublicationRepo.class);
        PublicationType _typeDouble = mock(PublicationType.class);
        Identifier _identifierDouble = mock(Identifier.class);
        Year _yearDouble = mock(Year.class);
        Title _titleDouble = mock(Title.class);
        Author _authorDouble = mock(Author.class);
        PublishingCompany _publisherDouble = mock(PublishingCompany.class);
        Edition _editionDouble = mock(Edition.class);
        Genre _genreDouble = mock(Genre.class);
        Publication expected = mock(Publication.class);
        when(_publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble))
                .thenReturn(expected);

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(_publicationRepo);

        //act
        Publication result = controller.registerPublication(
                _typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble
        );

        //assert
        assertSame(expected, result);
    }

    @Test
    void registerPublicationThrowsWhenRepoThrows() {
        //arrange
        PublicationRepo repoDouble = mock(PublicationRepo.class);
        when(repoDouble.addPublication(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Duplicate"));

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(repoDouble);

        //act and assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerPublication(
                        mock(PublicationType.class),
                        mock(Identifier.class),
                        mock(Year.class),
                        mock(Title.class),
                        mock(Author.class),
                        mock(PublishingCompany.class),
                        mock(Edition.class),
                        mock(Genre.class)
                )
        );
    }

    @Test
    void constructorThrowsWhenRepoIsNull() {
        //assert
        assertThrows(NullPointerException.class, () ->
                new RegisterNewPublicationController(null)
        );
    }

}