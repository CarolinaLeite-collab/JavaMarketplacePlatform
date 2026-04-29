package MITELOVERS.controller;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;
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
    private GenreFactory _genreFactoryDouble;
    private GenreId _genreIdDouble;

    @BeforeEach
    void setUp() {
        _iGenreRepoDouble = mock(IGenreRepo.class);
        _genreDouble = mock(Genre.class);
        _adminIdDouble = mock(UserId.class);
        _genreFactoryDouble = mock(GenreFactory.class);
        _genreIdDouble = mock(GenreId.class);
    }

    @Test
    void constructorAddGenreControllerShouldCreateController() {

        //SUT
        new AddGenreController(_iGenreRepoDouble, _genreFactoryDouble, _adminIdDouble);
    }


    @Test
    void addGenreShouldReturnGenreFromRepo() {
        //arrange
        String genreName = "Action";
        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(_genreDouble);
        when(_genreDouble.identity()).thenReturn(_genreIdDouble);
        when(_iGenreRepoDouble.containsOfIdentity(_genreDouble.identity())).thenReturn(false);
        when(_iGenreRepoDouble.save(_genreDouble)).thenReturn(_genreDouble);

        // SUT
        AddGenreController addGenreController = new AddGenreController(_iGenreRepoDouble, _genreFactoryDouble, _adminIdDouble);

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
        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(_genreDouble);
        when(_genreDouble.identity()).thenReturn(_genreIdDouble);
        when(_iGenreRepoDouble.containsOfIdentity(_genreDouble.identity())).thenReturn(false).thenReturn(true);
        when(_iGenreRepoDouble.save(_genreDouble)).thenReturn(_genreDouble);


        // SUT
        AddGenreController _addGenreController = new AddGenreController(_iGenreRepoDouble, _genreFactoryDouble, _adminIdDouble);

        //act
        Genre firstAddedGenre = _addGenreController.addGenre(genreName);

        //assert
        // Second attempt to add the same genre
        IllegalArgumentException secondAttemptThrows = assertThrows(IllegalArgumentException.class,  () -> _addGenreController.addGenre(genreName));

        assertNotNull(firstAddedGenre);
        assertEquals("Genre already exists in the repository", secondAttemptThrows.getMessage());
    }

}

