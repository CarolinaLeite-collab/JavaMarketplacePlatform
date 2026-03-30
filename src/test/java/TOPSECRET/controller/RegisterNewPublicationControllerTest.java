package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.edition.EditionBook;
import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Identifier;
import TOPSECRET.domain.valueobject.Title;
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
        PublicationType _typeDouble = mock(PublicationType.class);
        Identifier _identifierDouble = mock(Identifier.class);
        Year _yearDouble = mock(Year.class);
        Title _titleDouble = mock(Title.class);
        Author _authorDouble = mock(Author.class);
        PublishingCompany _publisherDouble = mock(PublishingCompany.class);
        EditionBook _editionBookDouble = mock(EditionBook.class);
        Genre _genreDouble = mock(Genre.class);
        Publication expected = mock(Publication.class);
        User _userDouble = mock(User.class);

        when(_iPublicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionBookDouble, _genreDouble))
                .thenReturn(expected);

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(_iPublicationRepo, _userDouble);

        //act
        Publication result = controller.registerPublication(
                _typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionBookDouble, _genreDouble
        );

        //assert
        assertSame(expected, result);
    }

    @Test
    void registerPublicationThrowsWhenRepoThrows() {
        //arrange
        User _userDouble = mock(User.class);

        IPublicationRepo iPublicationRepoDouble = mock(IPublicationRepo.class);
        when(iPublicationRepoDouble.addPublication(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Duplicate"));

        //SUT
        RegisterNewPublicationController controller = new RegisterNewPublicationController(iPublicationRepoDouble, _userDouble);

        //act and assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerPublication(
                        mock(PublicationType.class),
                        mock(Identifier.class),
                        mock(Year.class),
                        mock(Title.class),
                        mock(Author.class),
                        mock(PublishingCompany.class),
                        mock(EditionBook.class),
                        mock(Genre.class)
                )
        );
    }

    @Test
    void constructorThrowsWhenRepoIsNull() {
        //Arrange
        User _userDouble = mock(User.class);

        //assert
        assertThrows(NullPointerException.class, () ->
                new RegisterNewPublicationController(null, _userDouble)
        );
    }

}