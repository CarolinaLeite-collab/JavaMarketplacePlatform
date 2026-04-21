package MITELOVERS.controller;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddGenreControllerTest {

    private IGenreRepo _iGenreRepoDouble;
    private Genre _genreDouble;
    private UserId _adminIdDouble;

    @BeforeEach
    void setUp() {
        _iGenreRepoDouble = mock(IGenreRepo.class);
        _genreDouble = mock(Genre.class);
        _adminIdDouble = mock(UserId.class);
    }

    @Test
    void constructorAddGenreControllerShouldCreateController() {

        //SUT
        new AddGenreController(_iGenreRepoDouble, _adminIdDouble);
    }


    @Test
    void addGenreShouldReturnGenreFromRepo() {
        //arrange
        String genreName = "Action";
        when(_iGenreRepoDouble.addGenre(genreName)).thenReturn(_genreDouble);

        // SUT
        AddGenreController addGenreController = new AddGenreController(_iGenreRepoDouble, _adminIdDouble);

        // Act
        Genre genreAdded = addGenreController.addGenre(genreName);

        // Assert
        assertNotNull(genreAdded);
        assertEquals(_genreDouble, genreAdded);
    }

    @Test
    void addGenreThrowsWhenAlreadyExistsInRepo() {
        // Arrange
        String genreName = "Action";
        when(_iGenreRepoDouble.addGenre(genreName))
                .thenReturn(_genreDouble) // first call: genre is added
                .thenThrow(new IllegalArgumentException("This genre already exists"));     // second call: repo signals duplication

        // SUT
        AddGenreController _addGenreController = new AddGenreController(_iGenreRepoDouble, _adminIdDouble);

        //act
        Genre firstAddedGenre = _addGenreController.addGenre(genreName);

        //assert
        // Second attempt to add the same genre
        IllegalArgumentException secondAttemptThrows = assertThrows(IllegalArgumentException.class,  () -> _addGenreController.addGenre(genreName));

        assertNotNull(firstAddedGenre);
        assertEquals("This genre already exists", secondAttemptThrows.getMessage());
    }

}
