package TOPSECRET.controller;

import TOPSECRET.domain.User;
import TOPSECRET.domain.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterNewUserControllerTest {

    private RegisterNewUserController _registerNewUserController;
    private UserRepo _userRepoDouble;
    private User _adminDouble;
    private User _userDouble;

    @BeforeEach
    void setUp() {
        _userRepoDouble = mock(UserRepo.class);
        _adminDouble = mock(User.class);
        _userDouble = mock(User.class);

        when(_userRepoDouble.registerNewUser("Tiago", "test@email.pt")).thenReturn(_userDouble);

        _registerNewUserController = new RegisterNewUserController(_userRepoDouble, _adminDouble);
    }

    @Test
    void registerNewUserShouldCreateAndReturnUser() {
        // Arrange
        String name = "Tiago";
        String email = "test@email.pt";

        // Act
        User created = _registerNewUserController.registerNewUser(name, email);

        // Assert
        assertEquals(_userDouble, created);
        verify(_userRepoDouble).registerNewUser("Tiago", "test@email.pt");
    }

    @Test
    void registerNewUserShouldThrowIllegalStateExceptionWhenUserAlreadyExists() {
        // Arrange
        String name = "Someone Else";
        String email = "test@email.pt";

        when(_userRepoDouble.registerNewUser(name, email))
                .thenThrow(new IllegalStateException("User already exists"));

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _registerNewUserController.registerNewUser(name, email)
        );
    }
}