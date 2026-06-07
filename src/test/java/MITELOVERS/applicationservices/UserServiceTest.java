package MITELOVERS.applicationservices;

import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        User result = service.getUserByEmail("pedro@aeiou.com");

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
                service.getUserByEmail("unknown@aeiou.com"));
    }
}