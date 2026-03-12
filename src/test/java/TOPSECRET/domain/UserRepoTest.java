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
        //Arrange
        UserRepo repo = new UserRepo(_userFactoryDouble); //SUT

        //Act
        User result = repo.registerNewUser("Tiago", "tiago@example.com");

        //Assert
        assertEquals(_userDouble1, result);
    }

    @Test
    void shouldRegisterNewUserSuccessfullyAndListNotEmpty() {
        //Arrange
        UserRepo repo = new UserRepo(_userFactoryDouble); //SUT

        //Act
        repo.registerNewUser("Tiago", "tiago@example.com");

        //Assert
        assertEquals(1, repo.getAll().size());
    }

    @Test
    void shouldNotAllowDuplicateUsers() {
        //Arrange
        UserRepo repo = new UserRepo(_userFactoryDouble); //SUT

        repo.registerNewUser("Tiago", "tiago@example.com");

        //Act + Assert
        assertThrows(IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "tiago@example.com"));
    }

    @Test
    void shouldNotAllowDuplicateUsersIgnoringCaseAndSpaces() {
        //Arrange
        UserRepo repo = new UserRepo(_userFactoryDouble); //SUT

        repo.registerNewUser("Tiago", "tiago@example.com");

        //Act + Assert
        assertThrows(IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "  TiAgO@Example.com  "));
    }

    @Test
    void shouldBeAbleToRegisterMultipleUsers() {
        //Arrange
        UserRepo repo = new UserRepo(_userFactoryDouble); //SUT

        //Act
        repo.registerNewUser("Tiago", "tiago@example.com");
        repo.registerNewUser("Ana", "ana@example.com");

        //Assert
        assertEquals(2, repo.getAll().size());
    }

    @Test
    void shouldThrowCorrectMessageOnDuplicateUsers() {
        //Arrange
        UserRepo repo = new UserRepo(_userFactoryDouble); //SUT
        repo.registerNewUser("Tiago", "tiago@example.com");

        //Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "tiago@example.com")
        );

        //Assert
        assertEquals("User already exists", exception.getMessage());
    }
}