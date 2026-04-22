package MITELOVERS.controller;

import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterNewUserControllerTest {

    private IUserRepo _iUserRepoDouble;
    private UserFactory _userFactoryDouble;
    private User _adminDouble;
    private User _userDouble;
    private Name _nameDouble;
    private Address _addressDouble;
    private Email _emailDouble;
    private Phone _phoneDouble;
    private UserId _adminIdDouble;
    private UserId _userIdDouble;


    @BeforeEach
    void setUp() {
        _iUserRepoDouble = mock(IUserRepo.class);
        _userFactoryDouble = mock(UserFactory.class);
        _adminDouble = mock(User.class);
        _userDouble = mock(User.class);
        _nameDouble = mock(Name.class);
        _addressDouble = mock(Address.class);
        _emailDouble = mock(Email.class);
        _phoneDouble = mock(Phone.class);
        _adminIdDouble = mock(UserId.class);
        _userIdDouble = mock(UserId.class);

        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
    }


    @Test
    void shouldConstructController() {
        new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);
    }


    @Test
    void registerNewUserShouldCreateAndReturnUser() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(false);
        when(_iUserRepoDouble.save(_userDouble)).thenReturn(_userDouble);

        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        // Act
        User result = controller.registerNewUser(
                _adminDouble, _nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        // Assert
        assertEquals(_userDouble, result);
        verify(_userFactoryDouble).createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);
        verify(_iUserRepoDouble).save(_userDouble);
    }


    @Test
    void registerNewUserShouldCallSaveOnRepository() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(false);

        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        // Act
        controller.registerNewUser(
                _adminDouble, _nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        // Assert
        verify(_iUserRepoDouble).save(_userDouble);
    }


    @Test
    void registerNewUserNonAdminRoleThrowsSecurityException() {
        // Arrange
        User nonAdmin = mock(User.class);
        when(nonAdmin.hasRole(Role.ADMIN)).thenReturn(false);

        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        // Act & Assert
        assertThrows(SecurityException.class,
                () -> controller.registerNewUser(
                        nonAdmin, _nameDouble, _addressDouble, _emailDouble, _phoneDouble));
    }


    @Test
    void registerNewUserNonAdminShouldNeverCallFactory() {
        // Arrange
        User nonAdmin = mock(User.class);
        when(nonAdmin.hasRole(Role.ADMIN)).thenReturn(false);

        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        // Act
        assertThrows(SecurityException.class,
                () -> controller.registerNewUser(
                        nonAdmin, _nameDouble, _addressDouble, _emailDouble, _phoneDouble));

        // Assert
        verify(_userFactoryDouble, never()).createUser(any(), any(), any(), any());
    }


    @Test
    void registerNewUserShouldThrowWhenUserAlreadyExists() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(true);

        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(
                        _adminDouble, _nameDouble, _addressDouble, _emailDouble, _phoneDouble));
    }


    @Test
    void registerNewUserShouldThrowCorrectMessageWhenUserAlreadyExists() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(true);

        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(
                        _adminDouble, _nameDouble, _addressDouble, _emailDouble, _phoneDouble));

        // Assert
        assertEquals("User already exists", ex.getMessage());
    }


    @Test
    void registerNewUserDuplicateShouldNeverCallSave() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(true);

        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble, _adminIdDouble);

        // Act
        assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(
                        _adminDouble, _nameDouble, _addressDouble, _emailDouble, _phoneDouble));

        // Assert
        verify(_iUserRepoDouble, never()).save(any());
    }

}