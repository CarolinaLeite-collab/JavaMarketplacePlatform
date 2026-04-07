package TOPSECRET.controller;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.valueobject.UserId;
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
        //arrange
        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        //SUT
        new AddGenreController(_iGenreRepoDouble, _adminIdDouble);
    }

    @Test
    void addGenreThrowsWhenUserIsNotAdmin() {
        //arrange
        User _nonAdminDouble = mock(User.class);
        when(_nonAdminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        AddGenreController controller = new AddGenreController(_iGenreRepoDouble, _adminIdDouble);

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
        AddGenreController _addGenreController = new AddGenreController(_iGenreRepoDouble, _adminIdDouble);

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
        AddGenreController _addGenreController = new AddGenreController(_iGenreRepoDouble, _adminIdDouble);

        //act
        Genre firstAddedGenre = _addGenreController.addGenre(_adminDouble, genreName);

        //assert
        // Second attempt to add the same genre
        IllegalArgumentException secondAttemptThrows = assertThrows(IllegalArgumentException.class,  () -> _addGenreController.addGenre(_adminDouble, genreName));

        assertNotNull(firstAddedGenre);
        assertEquals("This genre already exists", secondAttemptThrows.getMessage());
    }

}
