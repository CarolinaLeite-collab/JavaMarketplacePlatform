package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddGenreControllerTest {

    private GenreRepo _genreRepoDouble;
    private Genre _genreDouble;
    private AddGenreController _addGenreController;

    @BeforeEach
    void setUp() {
        _genreRepoDouble = mock(GenreRepo.class);

    }

    @Test
    void constructorAddGenreControllerShouldCreateController() {
        //arrange
        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        //SUT
        new AddGenreController(_genreRepoDouble);
    }

    @Test
    void addGenreShouldReturnGenreFromRepo() {
        //arrange
        String genreName = "Action";

        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        _genreDouble = mock(Genre.class);
        when(_genreRepoDouble.addGenre(genreName)).thenReturn(_genreDouble);

        //SUT
        _addGenreController = new AddGenreController(_genreRepoDouble);

        //act
        Genre genreAdded = _addGenreController.addGenre(genreName, _adminDouble);

        //assert
        assertNotNull(genreAdded);
        assertEquals(_genreDouble, genreAdded);
        verify(_genreRepoDouble).addGenre(genreName);
    }

    @Test
    void addGenreThrowsWhenAlreadyExistsInRepo() {
        //arrange
        String genreName = "Action";

        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        _genreDouble = mock(Genre.class);

        when(_genreRepoDouble.addGenre(genreName))
                .thenReturn(_genreDouble) // first call: genre is added
                .thenThrow(new IllegalArgumentException("This genre already exists"));     // second call: repo signals duplication

        //SUT
        _addGenreController = new AddGenreController(_genreRepoDouble);

        //act
        Genre firstAddedGenre = _addGenreController.addGenre(genreName, _adminDouble);

        //assert
        // Second attempt to add the same genre
        IllegalArgumentException secondAttemptThrows = assertThrows(IllegalArgumentException.class,  () -> _addGenreController.addGenre(genreName, _adminDouble));

        assertNotNull(firstAddedGenre);
        assertEquals("This genre already exists", secondAttemptThrows.getMessage());
        verify(_genreRepoDouble, times(2)).addGenre(genreName); // proves repo was called twice
    }

    @Test
    void addGenreThrowsWhenUserIsNotAdmin() {
        //arrange
        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        String genreName = "Action";
        _genreDouble = mock(Genre.class);
        when(_genreDouble.getGenre()).thenReturn(genreName);

        //SUT
        _addGenreController = new AddGenreController(_genreRepoDouble);

        //act + assert
        assertThrows(SecurityException.class,  () -> _addGenreController.addGenre(genreName, _adminDouble));

    }

}
