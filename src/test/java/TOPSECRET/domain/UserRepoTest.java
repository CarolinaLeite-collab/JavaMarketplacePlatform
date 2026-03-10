package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRepoTest {

    private UserFactory _userFactoryDouble;
    private User _userDouble1;
    private User _userDouble2;

    @BeforeEach
    void setUp() {
        _userFactoryDouble = mock(UserFactory.class);
        _userDouble1 = mock(User.class);
        _userDouble2 = mock(User.class);

        when(_userFactoryDouble.createUser(any(Name.class), any(Email.class)))
                .thenReturn(_userDouble1, _userDouble2);

        when(_userDouble1.getEmail()).thenReturn("tiago@example.com");
        when(_userDouble2.getEmail()).thenReturn("ana@example.com");
    }

    @Test
    void registerNewUserShouldReturnUser() {
        UserRepo repo = new UserRepo(_userFactoryDouble);

        User result = repo.registerNewUser("Tiago", "tiago@example.com");

        assertEquals(_userDouble1, result);
    }

    @Test
    void shouldRegisterNewUserSuccessfullyAndListNotEmpty() {
        UserRepo repo = new UserRepo(_userFactoryDouble);

        repo.registerNewUser("Tiago", "tiago@example.com");

        assertEquals(1, repo.getAll().size());
    }

    @Test
    void shouldNotAllowDuplicateUsers() {
        UserRepo repo = new UserRepo(_userFactoryDouble);

        repo.registerNewUser("Tiago", "tiago@example.com");

        assertThrows(IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "tiago@example.com"));
    }

    @Test
    void shouldNotAllowDuplicateUsersIgnoringCaseAndSpaces() {
        UserRepo repo = new UserRepo(_userFactoryDouble);

        repo.registerNewUser("Tiago", "tiago@example.com");

        assertThrows(IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "  TiAgO@Example.com  "));
    }

    @Test
    void shouldBeAbleToRegisterMultipleUsers() {
        UserRepo repo = new UserRepo(_userFactoryDouble);

        repo.registerNewUser("Tiago", "tiago@example.com");
        repo.registerNewUser("Ana", "ana@example.com");

        assertEquals(2, repo.getAll().size());
    }

    @Test
    void shouldThrowCorrectMessageOnDuplicateUsers() {
        UserRepo repo = new UserRepo(_userFactoryDouble);
        repo.registerNewUser("Tiago", "tiago@example.com");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "tiago@example.com")
        );

        assertEquals("User already exists", exception.getMessage());
    }
}