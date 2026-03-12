package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddGenreControllerTest {

    private GenreRepo _genreRepoDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {
        _genreRepoDouble = mock(GenreRepo.class);
        _genreDouble = mock(Genre.class);
    }

    @Test
    void constructorAddGenreControllerShouldCreateController() {
        //arrange
        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        //SUT
        new AddGenreController(_genreRepoDouble, adminDouble);
    }

    @Test
    void addGenreThrowsWhenUserIsNotAdmin() {
        //arrange
        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        String genreName = "Action";
        when(_genreDouble.getGenre()).thenReturn(genreName);

        //act + assert
        assertThrows(SecurityException.class,  () -> new AddGenreController(_genreRepoDouble, _adminDouble)); //SUT

    }

    @Test
    void addGenreShouldReturnGenreFromRepo() {
        //arrange
        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        String genreName = "Action";
        when(_genreRepoDouble.addGenre(genreName)).thenReturn(_genreDouble);

        //SUT
        AddGenreController _addGenreController = new AddGenreController(_genreRepoDouble, _adminDouble);

        //act
        Genre genreAdded = _addGenreController.addGenre(genreName);

        //assert
        assertNotNull(genreAdded);
        assertEquals(_genreDouble, genreAdded);
        verify(_genreRepoDouble).addGenre(genreName);
    }

    @Test
    void addGenreThrowsWhenAlreadyExistsInRepo() {
        //arrange
        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        String genreName = "Action";
        when(_genreRepoDouble.addGenre(genreName))
                .thenReturn(_genreDouble) // first call: genre is added
                .thenThrow(new IllegalArgumentException("This genre already exists"));     // second call: repo signals duplication

        //SUT
        AddGenreController _addGenreController = new AddGenreController(_genreRepoDouble, _adminDouble);

        //act
        Genre firstAddedGenre = _addGenreController.addGenre(genreName);

        //assert
        // Second attempt to add the same genre
        IllegalArgumentException secondAttemptThrows = assertThrows(IllegalArgumentException.class,  () -> _addGenreController.addGenre(genreName));

        assertNotNull(firstAddedGenre);
        assertEquals("This genre already exists", secondAttemptThrows.getMessage());
    }

}
