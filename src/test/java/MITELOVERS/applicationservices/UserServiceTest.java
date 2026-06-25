package MITELOVERS.applicationservices;

import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private IUserRepo _userRepoDouble;

    @BeforeEach
    void setUp() {
        _userRepoDouble = mock(IUserRepo.class);
    }

    @Test
    void getUserByEmailReturnsUser() {
        // Arrange
        User userDouble = mock(User.class);
        when(_userRepoDouble.ofIdentity(any())).thenReturn(Optional.of(userDouble));

        // SUT
        UserService service = new UserService(_userRepoDouble);

        // Act
        User result = service.getUserByEmail(new UserId(new Email("pedro@aeiou.com")));

        // Assert
        assertNotNull(result);
    }

    @Test
    void getUserByEmailNotFoundThrowsException() {
        // Arrange
        when(_userRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // SUT
        UserService service = new UserService(_userRepoDouble);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                service.getUserByEmail(new UserId(new Email("unknown@aeiou.com"))));
    }

    @Test
    void userIdExistsShouldReturnTrueIfUserExists() {
        // Arrange
        String emailString = "seller@selling.com";

        when(_userRepoDouble.containsOfIdentity(any())).thenReturn(true);

        // SUT
        UserService service = new UserService(_userRepoDouble);

        // Act
        boolean result = service.userIdExists(emailString);

        // Assert
        assertTrue(result);
    }

    @Test
    void userIdExistsShouldReturnFalseIfUserDoesNotExist() {
        // Arrange
        String emailString = "seller@selling.com";

        when(_userRepoDouble.containsOfIdentity(any())).thenReturn(false);

        // SUT
        UserService service = new UserService(_userRepoDouble);

        // Act
        boolean result = service.userIdExists(emailString);

        // Assert
        assertFalse(result);

    }
}