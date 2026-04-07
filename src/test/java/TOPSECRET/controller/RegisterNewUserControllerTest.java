package TOPSECRET.controller;
import TOPSECRET.domain.repository.IUserRepo;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.user.UserFactory;
import TOPSECRET.domain.valueobject.Email;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterNewUserControllerTest {

    private IUserRepo _iUserRepoDouble;
    private UserFactory _userFactoryDouble;
    private User _adminDouble;
    private User _userDouble;

    @BeforeEach
    void setUp() {
        _iUserRepoDouble = mock(IUserRepo.class);
        _userFactoryDouble = mock(UserFactory.class);
        _adminDouble = mock(User.class);
        _userDouble = mock(User.class);

        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
    }

    @Test
    void shouldConstructController() {
        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble);

        assertNotNull(controller);
    }

    @Test
    void registerNewUserShouldCreateAndReturnUser() {
        when(_iUserRepoDouble.containsOfIdentity(any(UserId.class))).thenReturn(false);
        when(_userFactoryDouble.createUser(any(Name.class), any(Email.class))).thenReturn(_userDouble);
        doReturn(_userDouble).when(_iUserRepoDouble).save(any(User.class));

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble);

        User result = controller.registerNewUser(_adminDouble, "Tiago", "tiago@example.com");

        assertEquals(_userDouble, result);
        verify(_iUserRepoDouble).save(_userDouble);
    }

    @Test
    void registerNewUserNonAdminRoleThrowsSecurityException() {
        User nonAdmin = mock(User.class);
        when(nonAdmin.hasRole(Role.ADMIN)).thenReturn(false);

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble);

        assertThrows(SecurityException.class,
                () -> controller.registerNewUser(nonAdmin, "Tiago", "tiago@example.com"));
    }

    @Test
    void registerNewUserShouldThrowWhenUserAlreadyExists() {
        when(_iUserRepoDouble.containsOfIdentity(any(UserId.class))).thenReturn(true);

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble);

        assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(_adminDouble, "Tiago", "tiago@example.com"));
    }

    @Test
    void registerNewUserShouldThrowCorrectMessageWhenUserAlreadyExists() {
        when(_iUserRepoDouble.containsOfIdentity(any(UserId.class))).thenReturn(true);

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(_adminDouble, "Tiago", "tiago@example.com"));

        assertEquals("User already exists", ex.getMessage());
    }
}