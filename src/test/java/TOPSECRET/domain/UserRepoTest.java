package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepoTest {

    private UserRepo repo;

    @BeforeEach
    void setUp() {
        repo = new UserRepo();
    }

    @Test
    void registerNewUser_shouldCreateAndReturnUser_whenValidInputs() {
        // arrange
        String name = "Tiago";
        String email = "tiago@example.com";

        // act
        User created = repo.registerNewUser(name, email);

        // assert
        assertNotNull(created);
        assertEquals("tiago@example.com", created.getEmail());
    }

    @Test
    void registerNewUser_shouldThrowIllegalStateException_whenUserAlreadyExists_sameExactEmail() {
        // arrange
        repo.registerNewUser("Tiago", "tiago@example.com");

        // act + assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "tiago@example.com")
        );

        assertEquals("User already exists", ex.getMessage());
    }

    @Test
    void registerNewUser_shouldThrowIllegalStateException_whenUserAlreadyExists_ignoreCaseAndSpaces() {
        // arrange
        repo.registerNewUser("Tiago", "tiago@example.com");

        // act + assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> repo.registerNewUser("Outro", "  TiAgO@Example.com  ")
        );

        assertEquals("User already exists", ex.getMessage());
    }

    @Test
    void registerNewUser_shouldAllowDifferentEmails() {
        // arrange + act
        User u1 = repo.registerNewUser("Tiago", "tiago@example.com");
        User u2 = repo.registerNewUser("Ana", "ana@example.com");

        // assert
        assertNotNull(u1);
        assertNotNull(u2);
        assertNotEquals(u1.getEmail(), u2.getEmail());
    }
}