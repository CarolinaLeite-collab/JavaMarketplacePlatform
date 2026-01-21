package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterNewUserControllerTest {

private RegisterNewUserController _registerNewUserController;

    @BeforeEach
    void setUp() {
        UserRepo repo = new UserRepo();
        Name name = new Name ("Name");
        Email email = new Email ("name@email.pt");
        User user = new User(name, email);
        _registerNewUserController = new RegisterNewUserController (repo, user);
    }

    @Test
    void registerNewUser_shouldCreateAndReturnUser() {
        // arrange
        // act
        User created = _registerNewUserController.RegisterNewUser("Tiago", "test@email.pt");

        // assert
        assertNotNull(created);
        assertEquals("test@email.pt", created.getEmail());
    }

    @Test
    void registerNewUser_shouldThrowIllegalStateException_whenUserAlreadyExists() {
        // arrange

        _registerNewUserController.RegisterNewUser("Tiago", "test@email.pt");

        // act + assert
        assertThrows(IllegalStateException.class,
                () -> _registerNewUserController.RegisterNewUser("Someone Else", "test@email.pt"));
    }
}