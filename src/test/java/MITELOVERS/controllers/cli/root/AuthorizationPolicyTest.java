package MITELOVERS.controllers.cli.root;

import MITELOVERS.controllers.cli.root.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorizationPolicyTest {

    private final AuthorizationPolicy policy = new AuthorizationPolicy();

    @Test
    void userCanListGenres() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canListGenres(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanListGenres() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canListGenres(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotListGenres() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canListGenres(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void adminCanAddGenre() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canAddGenre(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userCannotAddGenre() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);
        when(user.hasRole(Role.ADMIN)).thenReturn(false);

        // Act
        boolean result = policy.canAddGenre(user);

        // Assert
        assertFalse(result);
    }

}