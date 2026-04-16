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
        Author result = controller.createAuthor("Tolstói");

        // Assert
        assertEquals(_authorDouble, result);

    }

    @Test
    void shouldTrimAuthorNameBeforeSaving() {

        // Arrange
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iAuthorRepoDouble.addAuthor("Tolstói")).thenReturn(_authorDouble);

        // SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userIdDouble);

        // Act
        controller.createAuthor("   Tolstói   ");

        // Assert
        verify(_iAuthorRepoDouble).addAuthor("Tolstói");

    }


}