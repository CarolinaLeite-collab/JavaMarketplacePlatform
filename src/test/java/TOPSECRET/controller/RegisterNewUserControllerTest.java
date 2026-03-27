package TOPSECRET.controller;

import TOPSECRET.domain.IUserRepo;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegisterNewUserControllerTest {

    private IUserRepo _iUserRepoDouble;
    private User _adminDouble;
    private User _userDouble;

    @BeforeEach
    void setUp() {
        _iUserRepoDouble = mock(IUserRepo.class);
        _adminDouble = mock(User.class);
        _userDouble = mock(User.class);

        when(_iUserRepoDouble.registerNewUser("Tiago", "test@email.pt")).thenReturn(_userDouble);

    }

    @Test
    void shouldConstructController() {
        //Act & SUT
        RegisterNewUserController registerNewUserController = new RegisterNewUserController(_iUserRepoDouble, _adminDouble);
    }

    @Test
    void registerNewUserShouldCreateAndReturnUser() {
        // Arrange
        String name = "Tiago";
        String email = "test@email.pt";

        //SUT
        RegisterNewUserController registerNewUserController = new RegisterNewUserController(_iUserRepoDouble, _adminDouble);

        // Act
        User created = registerNewUserController.registerNewUser(name, email);

        // Assert
        assertEquals(_userDouble, created);
        verify(_iUserRepoDouble).registerNewUser("Tiago", "test@email.pt");
    }

    @Test
    void registerNewUserShouldThrowIllegalStateExceptionWhenUserAlreadyExists() {
        // Arrange
        String name = "Someone Else";
        String email = "test@email.pt";

        when(_iUserRepoDouble.registerNewUser(name, email)).thenThrow(new IllegalStateException("User already exists"));

        //SUT
        RegisterNewUserController _registerNewUserController = new RegisterNewUserController(_iUserRepoDouble, _adminDouble);

        // Act + Assert
        assertThrows(IllegalStateException.class, () -> _registerNewUserController.registerNewUser(name, email));
    }

}