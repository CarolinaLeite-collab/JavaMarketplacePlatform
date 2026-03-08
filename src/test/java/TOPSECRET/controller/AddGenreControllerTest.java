package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddGenreControllerTest {


    private User _adminDouble;
    private GenreRepo _genreRepoDouble;
    private Genre _genreDouble;
    private AddGenreController _addGenreController;

    @BeforeEach
    void setUp() {
        _adminDouble = mock(User.class);
        _genreRepoDouble = mock(GenreRepo.class);
        _genreDouble = mock(Genre.class);

        //SUT
        _addGenreController = new AddGenreController(_genreRepoDouble, _adminDouble);

    }

    @Test
    void constructorAddGenreControllerShouldCreateController() {
        // AddGenreController controller = new AddGenreController(_genreRepoDouble, _adminDouble);

        // _addGenreController is already created in @BeforeEach with mocked dependencies
        assertNotNull(_addGenreController);
    }

    @Test
    void addGenreShouldReturnGenreFromRepo() {
        String genreName = "Action";

        when(_genreRepoDouble.addGenre(genreName)).thenReturn(_genreDouble);

        Genre genreAdded = _addGenreController.addGenre(genreName);

        assertNotNull(genreAdded);
        assertEquals(_genreDouble, genreAdded);
        verify(_genreRepoDouble).addGenre(genreName);
    }

    @Test
    void addGenreThrowsWhenAlreadyExistsInRepo() {
        String genreName = "Action";

        when(_genreRepoDouble.addGenre(genreName))
                .thenReturn(_genreDouble) // first call: genre is added
                .thenThrow(new IllegalArgumentException("This genre already exists"));     // second call: repo signals duplication

        Genre firstAddedGenre = _addGenreController.addGenre(genreName);

        // Second attempt to add the same genre
        IllegalArgumentException secondAttemptThrows = assertThrows(IllegalArgumentException.class,  () -> _addGenreController.addGenre(genreName));

        assertNotNull(firstAddedGenre);
        assertEquals("This genre already exists", secondAttemptThrows.getMessage());
        verify(_genreRepoDouble, times(2)).addGenre(genreName); // proves repo was called twice
    }

}
