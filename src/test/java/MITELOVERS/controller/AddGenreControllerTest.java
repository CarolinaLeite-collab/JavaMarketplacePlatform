package MITELOVERS.controller;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@WebMvcTest(AddGenreController.class)
@ActiveProfiles("jpa")
class AddGenreControllerTest {

    @Autowired
    private AddGenreController _sut;

    @MockBean
    private IGenreRepo _iGenreRepoDouble;

    @MockBean
    private GenreFactory _genreFactoryDouble;

    @MockBean
    private UserId _adminIdDouble;

    @Test
    void addGenreShouldReturnGenreFromRepo() {
        // Arrange
        String genreName = "Action";
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(false);
        when(_iGenreRepoDouble.save(genreDouble)).thenReturn(genreDouble);

        // Act
        Genre genreAdded = _sut.addGenre(genreName);

        // Assert
        assertNotNull(genreAdded);
        assertEquals(genreDouble, genreAdded);
        verify(_iGenreRepoDouble).save(genreDouble);
    }

    @Test
    void addGenreThrowsWhenAlreadyExistsInRepo() {
        // Arrange
        String genreName = "Action";
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(true);

        // Act // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> _sut.addGenre(genreName));

        assertEquals("Genre already exists in the repository", exception.getMessage());
        verify(_iGenreRepoDouble, never()).save(any());
    }
}