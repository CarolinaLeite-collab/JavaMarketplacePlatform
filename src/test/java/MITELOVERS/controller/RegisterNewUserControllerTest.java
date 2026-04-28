package MITELOVERS.controller;

import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RegisterNewUserControllerTest {

    @MockBean
    IUserRepo _iUserRepoDouble;

    @MockBean
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

        MockitoAnnotations.openMocks(this);

        _userDouble = mock(User.class);
        _nameDouble = mock(Name.class);
        _addressDouble = mock(Address.class);
        _emailDouble = mock(Email.class);
        _phoneDouble = mock(Phone.class);
        _userIdDouble = mock(UserId.class);
    }

    @Test
    void registerNewUserControllerTest() {
        // SUT
        _registerNewUserController = new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble);
    }

    @Test
    void registerNewUserShouldCreateAndReturnUser() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(false);
        when(_iUserRepoDouble.save(_userDouble)).thenReturn(_userDouble);

        // SUT
        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble);

        // Act
        User result = controller.registerNewUser(
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

        // SUT
        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(
                        _nameDouble, _addressDouble, _emailDouble, _phoneDouble));
    }

    @Test
    void registerNewUserShouldThrowCorrectMessageWhenUserAlreadyExists() {
        // Arrange
        when(_userFactoryDouble.createUser(_nameDouble, _addressDouble, _emailDouble, _phoneDouble))
                .thenReturn(_userDouble);
        when(_userDouble.identity()).thenReturn(_userIdDouble);
        when(_iUserRepoDouble.containsOfIdentity(_userIdDouble)).thenReturn(true);

        // SUT
        RegisterNewUserController controller =
                new RegisterNewUserController(_iUserRepoDouble, _userFactoryDouble);

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> controller.registerNewUser(
                        _nameDouble, _addressDouble, _emailDouble, _phoneDouble));

        // Assert
        assertEquals("User already exists", ex.getMessage());
    }


}