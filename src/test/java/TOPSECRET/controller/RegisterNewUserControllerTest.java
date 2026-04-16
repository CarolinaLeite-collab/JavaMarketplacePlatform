package TOPSECRET.controller;

import TOPSECRET.domain.repository.IUserRepo;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterNewUserControllerTest {

    private IUserRepo _iUserRepoDouble;
    private User _adminDouble;
    private User _userDouble;
    private Name _nameDouble;
    private Address _addressDouble;
    private Email _emailDouble;
    private Phone _phoneDouble;
    private UserId _adminIdDouble;

    @BeforeEach
    void setUp() {
        _iUserRepoDouble = mock(IUserRepo.class);
        _adminDouble = mock(User.class);
        _userDouble = mock(User.class);
        _nameDouble = mock(Name.class);
        _addressDouble = mock(Address.class);
        _emailDouble = mock(Email.class);
        _phoneDouble = mock(Phone.class);
        _adminIdDouble = mock(UserId.class);

        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
    }

    @Test
    void shouldConstructController() {
        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _adminIdDouble);

        assertNotNull(controller);
    }

    @Test
    void registerNewUserShouldCreateAndReturnUser() {
        when(_iUserRepoDouble.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _adminIdDouble);

        User result = controller.registerNewUser(_adminDouble, _nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        assertEquals(_userDouble, result);
        verify(_iUserRepoDouble).addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);
    }

    @Test
    void registerNewUserNonAdminRoleThrowsSecurityException() {
        User nonAdmin = mock(User.class);
        when(nonAdmin.hasRole(Role.ADMIN)).thenReturn(false);

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _adminIdDouble);

        assertThrows(SecurityException.class,
                () -> controller.registerNewUser(nonAdmin, _nameDouble, _addressDouble, _emailDouble, _phoneDouble));
    }

    @Test
    void registerNewUserShouldThrowWhenUserAlreadyExists() {
        when(_iUserRepoDouble.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenThrow(new IllegalStateException("User already exists"));

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _adminIdDouble);

        assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(_adminDouble, _nameDouble, _addressDouble, _emailDouble, _phoneDouble));
    }

    @Test
    void registerNewUserShouldThrowCorrectMessageWhenUserAlreadyExists() {
        when(_iUserRepoDouble.addUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenThrow(new IllegalStateException("User already exists"));

        RegisterNewUserController controller = new RegisterNewUserController(_iUserRepoDouble, _adminIdDouble);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(_adminDouble, _nameDouble, _addressDouble, _emailDouble, _phoneDouble));

        assertEquals("User already exists", ex.getMessage());
    }
}