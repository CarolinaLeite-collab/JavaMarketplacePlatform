package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.applicationservices.PublicationService;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class RegisterNewPublicationControllerTest {

    //SUT
    @InjectMocks
    RegisterNewPublicationController _controller;

    @Mock
    PublicationService _publicationServiceDouble;

    @Mock
    AuthorService _authorServiceDouble;

    @Mock
    GenreService _genreServiceDouble;

    String exception = "";

    @Test
    void registerPublicationCallsServiceWithCorrectArguments() {
        //Arrange
        Year yearDouble = mock(Year.class);
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        Publication publicationDouble = mock(Publication.class);
        String synopsis = "Synopsis";

        when(_publicationServiceDouble.registerPublication(
                titleDouble,
                authorIdDouble,
                yearDouble,
                genreIdDouble,
                synopsis
        )).thenReturn(publicationDouble);

        //Act
        Publication result = _controller.registerPublication(
                titleDouble,
                authorIdDouble,
                yearDouble,
                genreIdDouble,
                synopsis
        );

        //Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void registerPublicationThrowsWhenServiceThrows() {
        //Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year yearDouble = mock(Year.class);
        GenreId genreIdDouble = mock(GenreId.class);
        String synopsis = "Synopsis";

        when(_publicationServiceDouble.registerPublication(
                titleDouble,
                authorIdDouble,
                yearDouble,
                genreIdDouble,
                synopsis
        )).thenThrow(new IllegalArgumentException("Duplicate"));

        //Assert
        assertThrows(IllegalArgumentException.class, () ->
                _controller.registerPublication(
                        titleDouble,
                        authorIdDouble,
                        yearDouble,
                        genreIdDouble,
                        synopsis
                )
        );

    }

    @Test
    void shouldReturnAllAuthorIds() {
        //Arrange
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_authorServiceDouble.getAuthorsId())
                .thenReturn(List.of(authorIdDouble));

        Iterable<AuthorId> result = _controller.getAuthorsId();

        //Act
        List<AuthorId> ids = new ArrayList<>();
        result.forEach(ids::add);

        //Assert
        assertEquals(authorIdDouble, ids.get(0));
    }

    @Test
    void shouldReturnAllGenreIds() {
        //Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        when(_genreServiceDouble.getGenresId())
                .thenReturn(List.of(genreIdDouble));

        Iterable<GenreId> result = _controller.getGenresId();

        //Act
        List<GenreId> ids = new ArrayList<>();
        result.forEach(ids::add);

        //Assert
        assertEquals(genreIdDouble, ids.get(0));
    }

}
