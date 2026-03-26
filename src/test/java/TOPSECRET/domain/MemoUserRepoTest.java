package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Email;
import TOPSECRET.domain.valueobject.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoUserRepoTest {

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

        when(_userDouble1.hasEmail(any(Email.class))).thenReturn(true);

    }

    @Test
    void registerNewUserShouldReturnUser() {
        //Arrange
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble); //SUT

        //Act
        User result = repo.registerNewUser("Tiago", "tiago@example.com");

        //Assert
        assertEquals(_userDouble1, result);
    }

    @Test
    void shouldRegisterNewUserSuccessfullyAndListNotEmpty() {
        //Arrange
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble); //SUT

        //Act
        repo.registerNewUser("Tiago", "tiago@example.com");

        //Assert
        assertEquals(1, repo.getAll().size());
    }

    @Test
    void shouldNotAllowDuplicateUsers() {

        //Arrange
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble); //SUT

        repo.registerNewUser("Tiago", "tiago@example.com");

        //Act + Assert
        assertThrows(IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "tiago@example.com"));
    }

    @Test
    void shouldBeAbleToRegisterMultipleUsers() {

        //Arrange
        when(_userDouble1.hasEmail(new Email("ana@example.com"))).thenReturn(false);
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble); //SUT

        //Act
        repo.registerNewUser("Tiago", "tiago@example.com");
        repo.registerNewUser("Ana", "ana@example.com");

        //Assert
        assertEquals(2, repo.getAll().size());
    }

    @Test
    void shouldThrowCorrectMessageOnDuplicateUsers() {
        //Arrange
        MemoUserRepo repo = new MemoUserRepo(_userFactoryDouble); //SUT

        //Act
        repo.registerNewUser("Tiago", "tiago@example.com");


        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "tiago@example.com")
        );

        //Assert
        assertEquals("User already exists", exception.getMessage());
    }
}