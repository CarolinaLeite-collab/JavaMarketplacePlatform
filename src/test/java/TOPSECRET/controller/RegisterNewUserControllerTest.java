package TOPSECRET.controller;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.repository.IUserRepo;
import TOPSECRET.domain.valueobject.Email;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.Role;
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
    private UserId _adminIdDouble;

    @BeforeEach
    void setUp() {
        _iUserRepoDouble = mock(IUserRepo.class);
        _userFactoryDouble = mock(UserFactory.class);
        _adminDouble = mock(User.class);
        _userDouble = mock(User.class);
        _adminIdDouble = mock(UserId.class);

        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
    }

    @Test
    void shouldConstructController() {
        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        assertNotNull(controller);
    }

    @Test
    void registerNewUserShouldCreateAndReturnUser() {
        when(_iUserRepoDouble.containsOfIdentity(any(UserId.class))).thenReturn(false);
        when(_userFactoryDouble.createUser(any(Name.class), any(Email.class))).thenReturn(_userDouble);
        doReturn(_userDouble).when(_iUserRepoDouble).save(any(User.class));

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        User result = controller.registerNewUser(_adminDouble, "Tiago", "tiago@example.com");

        assertEquals(_userDouble, result);
        verify(_iUserRepoDouble).save(_userDouble);
    }

    @Test
    void registerNewUserNonAdminRoleThrowsSecurityException() {
        User nonAdmin = mock(User.class);
        when(nonAdmin.hasRole(Role.ADMIN)).thenReturn(false);

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        assertThrows(SecurityException.class,
                () -> controller.registerNewUser(nonAdmin, "Tiago", "tiago@example.com"));
    }

    @Test
    void registerNewUserShouldThrowWhenUserAlreadyExists() {
        when(_iUserRepoDouble.containsOfIdentity(any(UserId.class))).thenReturn(true);

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(_adminDouble, "Tiago", "tiago@example.com"));
    }

    @Test
    void registerNewUserShouldThrowCorrectMessageWhenUserAlreadyExists() {
        when(_iUserRepoDouble.containsOfIdentity(any(UserId.class))).thenReturn(true);

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(_adminDouble, "Tiago", "tiago@example.com"));

        assertEquals("User already exists", ex.getMessage());
    }
}