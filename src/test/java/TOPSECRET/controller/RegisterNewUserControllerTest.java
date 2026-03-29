package TOPSECRET.controller;
import TOPSECRET.domain.IUserRepo;
import TOPSECRET.domain.Role;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterNewUserControllerTest {

    private IUserRepo _iUserRepoDouble;

    @BeforeEach
    void setUp() {
        _iUserRepoDouble = mock(IUserRepo.class);
    }

    @Test
    void shouldConstructController() {
        //Act & SUT
        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void registerNewUserShouldCreateAndReturnUser() {
        // Arrange
       User _adminDouble = mock(User.class);
       User _userDouble = mock(User.class);
       when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
       when(_iUserRepoDouble.registerNewUser("Tiago", "test@email.pt")).thenReturn(_userDouble);

        //SUT
        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble);

        // Act
        User created = controller.registerNewUser(_adminDouble, "Tiago", "test@email.pt");

        // Assert
        assertEquals(_userDouble, created);
        verify(_iUserRepoDouble).registerNewUser("Tiago", "test@email.pt");
    }

    @Test
    void registerNewUserNonAdminRoleThrowsSecurityException() {
        // Arrange
        User _nonAdminDouble = mock(User.class);
        when(_nonAdminDouble.hasRole(Role.ADMIN)).thenReturn(false);
        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble);

        // SUT & Assert
        assertThrows(SecurityException.class,
                () -> controller.registerNewUser(_nonAdminDouble, "Tiago", "test@email.pt"));
    }

    @Test
    void registerNewUserShouldThrowIllegalStateExceptionWhenUserAlreadyExists() {
        // Arrange
        User _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iUserRepoDouble.registerNewUser("Someone", "test@email.pt"))
                .thenThrow(new IllegalStateException("User already exists"));

        //SUT
        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble);

        // Act + Assert
        assertThrows(IllegalStateException.class, () -> controller.registerNewUser(_adminDouble, "Someone", "test@email.pt"));
    }

}