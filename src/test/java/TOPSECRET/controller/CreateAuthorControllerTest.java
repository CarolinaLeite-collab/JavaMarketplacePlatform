package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuthorRepo;
import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class CreateAuthorControllerTest {

    private IAuthorRepo _iAuthorRepoDouble;
    private User _adminDouble;
    private Author _authorDouble;
    private UserId _userIdDouble;

    @BeforeEach
    void setUp() {

        _iAuthorRepoDouble = mock(IAuthorRepo.class);
        _adminDouble = mock(User.class);
        _authorDouble = mock(Author.class);
        _userIdDouble = mock(UserId.class);

    }


    @Test
    void testCreateAuthorControllerConstructor() {

        // SUT & Act
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userIdDouble);

    }

    @Test
    void shouldCreateAuthorWhenUserIsAdmin() {

        // Arrange
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iAuthorRepoDouble.addAuthor("Tolstói")).thenReturn(_authorDouble);

        // SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userIdDouble);

        // Act
        Author result = controller.createAuthor("Tolstói", _adminDouble);

        // Assert
        assertEquals(_authorDouble, result);

    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAdmin() {

        // Arrange
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userIdDouble);

        // Act
        Executable action = () -> controller.createAuthor("Tolstói", _adminDouble);

        // Assert
        assertThrows(SecurityException.class, action);

    }

    @Test
    void shouldTrimAuthorNameBeforeSaving() {

        // Arrange
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iAuthorRepoDouble.addAuthor("Tolstói")).thenReturn(_authorDouble);

        // SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userIdDouble);

        // Act
        controller.createAuthor("   Tolstói   ", _adminDouble);

        // Assert
        verify(_iAuthorRepoDouble).addAuthor("Tolstói");

    }

    @Test
    void shouldNotCallRepoWhenUserIsNotAdmin() {

        // Arrange
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userIdDouble);

        // Act
        Executable action = () -> controller.createAuthor("Tolstói", _adminDouble);

        // Assert
        assertThrows(SecurityException.class, action);

    }

}