package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddGenreControllerTest {

    private IGenreRepo _iGenreRepoDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {
        _iGenreRepoDouble = mock(IGenreRepo.class);
        _genreDouble = mock(Genre.class);
    }

    @Test
    void constructorAddGenreControllerShouldCreateController() {
        //arrange
        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        //SUT
        new AddGenreController(_iGenreRepoDouble);
    }

    @Test
    void addGenreThrowsWhenUserIsNotAdmin() {
        //arrange
        User _nonAdminDouble = mock(User.class);
        when(_nonAdminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        AddGenreController controller = new AddGenreController(_iGenreRepoDouble);

        // Act + Assert
        assertThrows(SecurityException.class,
                () -> controller.addGenre(_nonAdminDouble, "Action"));
    }

    @Test
    void addGenreShouldReturnGenreFromRepo() {
        //arrange
        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        String genreName = "Action";
        when(_iGenreRepoDouble.addGenre(genreName)).thenReturn(_genreDouble);

        //SUT
        AddGenreController _addGenreController = new AddGenreController(_iGenreRepoDouble);

        //act
        Genre genreAdded = _addGenreController.addGenre(_adminDouble, genreName);

        //assert
        assertNotNull(genreAdded);
        assertEquals(_genreDouble, genreAdded);
    }

    @Test
    void addGenreThrowsWhenAlreadyExistsInRepo() {
        //arrange
        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        String genreName = "Action";
        when(_iGenreRepoDouble.addGenre(genreName))
                .thenReturn(_genreDouble) // first call: genre is added
                .thenThrow(new IllegalArgumentException("This genre already exists"));     // second call: repo signals duplication

        //SUT
        AddGenreController _addGenreController = new AddGenreController(_iGenreRepoDouble);

        //act
        Genre firstAddedGenre = _addGenreController.addGenre(_adminDouble, genreName);

        //assert
        // Second attempt to add the same genre
        IllegalArgumentException secondAttemptThrows = assertThrows(IllegalArgumentException.class,  () -> _addGenreController.addGenre(_adminDouble, genreName));

        assertNotNull(firstAddedGenre);
        assertEquals("This genre already exists", secondAttemptThrows.getMessage());
    }

}
