package MITELOVERS.controller;

import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class RegisterNewUserControllerTest {

    @Mock
    IUserRepo _iUserRepoDouble;

    @Mock
    UserFactory _userFactoryDouble;

    @InjectMocks
    RegisterNewUserController _registerNewUserController;

    private User _userDouble;
    private Name _nameDouble;
    private Address _addressDouble;
    private Email _emailDouble;
    private Phone _phoneDouble;
    private UserId _userIdDouble;


    @BeforeEach
    void setUp() throws InstantiationException{

        _userDouble = mock(User.class);
        _nameDouble = mock(Name.class);
        _addressDouble = mock(Address.class);
        _emailDouble = mock(Email.class);
        _phoneDouble = mock(Phone.class);
        _userIdDouble = mock(UserId.class);

    }

    @Test
    void registerNewUserShouldCreateAndReturnUser() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(false);
        when(_iUserRepoDouble.save(_userDouble)).thenReturn(_userDouble);

        // Act
        User result = _registerNewUserController.registerNewUser(
                _nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        // Assert
        assertEquals(_userDouble, result);
        verify(_userFactoryDouble).createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);
        verify(_iUserRepoDouble).save(_userDouble);
    }


    @Test
    void registerNewUserShouldThrowWhenUserAlreadyExists() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(true);


        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _registerNewUserController.registerNewUser(
                        _nameDouble, _addressDouble, _emailDouble, _phoneDouble));
    }

    @Test
    void registerNewUserShouldThrowCorrectMessageWhenUserAlreadyExists() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(true);

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> _registerNewUserController.registerNewUser(
                        _nameDouble, _addressDouble, _emailDouble, _phoneDouble));

        // Assert
        assertEquals("User already exists", ex.getMessage());
    }


}