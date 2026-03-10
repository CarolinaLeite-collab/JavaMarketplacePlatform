package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterNewPublicationControllerTest {

    @Test
    void registerPublication_callsRepoWithCorrectArguments() {
        PublicationRepo _publicationRepo = mock(PublicationRepo.class);
        RegisterNewPublicationController controller = new RegisterNewPublicationController(_publicationRepo);

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

        Publication result = controller.registerPublication(
                _typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble
        );

        assertSame(expected, result);
    }

    @Test
    void registerPublication_throwsWhenRepoThrows() {
        PublicationRepo repo = mock(PublicationRepo.class);
        RegisterNewPublicationController controller = new RegisterNewPublicationController(repo);

        when(repo.addPublication(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Duplicate"));

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
    void constructor_throwsWhenRepoIsNull() {
        assertThrows(NullPointerException.class, () ->
                new RegisterNewPublicationController(null)
        );
    }

}