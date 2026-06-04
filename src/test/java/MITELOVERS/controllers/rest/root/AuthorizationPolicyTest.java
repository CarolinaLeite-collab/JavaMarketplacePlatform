package MITELOVERS.controllers.rest.root;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorizationPolicyTest {

    private final AuthorizationPolicy policy = new AuthorizationPolicy();

    @Test
    void userCanListGenres() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        //Act
        boolean result = policy.canListGenres(user);

        //Assert
        assertTrue(result);
    }

    @Test
    void adminCanListGenres() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        //Act
        boolean result = policy.canListGenres(user);

        //Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotListGenres() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        //Act
        boolean result = policy.canListGenres(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void adminCanAddGenre() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        //Act
        boolean result = policy.canAddGenre(user);

        //Assert
        assertTrue(result);
    }

    @Test
    void userCannotAddGenre() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);
        when(user.hasRole(Role.ADMIN)).thenReturn(false);

        //Act
        boolean result = policy.canAddGenre(user);

        //Assert
        assertFalse(result);
    }

    @Test
    void userCanViewLibrary() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        //Act
        boolean result = policy.canViewLibrary(user);

        //Assert
        assertTrue(result);
    }

    @Test
    void adminCanViewLibrary() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        //Act
        boolean result = policy.canViewLibrary(user);

        //Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotViewLibrary() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        //Act
        boolean result = policy.canViewLibrary(user);

        //Assert
        assertFalse(result);
    }

    @Test
    void userCanCreateList() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        //Act
        boolean result = policy.canCreateList(user);

        //Assert
        assertTrue(result);
    }

    @Test
    void adminCanCreateList() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        //Act
        boolean result = policy.canCreateList(user);

        //Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotCreateList() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        //Act
        boolean result = policy.canCreateList(user);

        //Assert
        assertFalse(result);
    }

    @Test
    void userCanSell() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        //Act
        boolean result = policy.canSell(user);

        //Assert
        assertTrue(result);
    }

    @Test
    void adminCanSell() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canSell(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotSell() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canSell(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanListPublications() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canListPublications(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotListPublications() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canListPublications(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanCreatePublication() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canCreatePublication(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotCreatePublication() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canCreatePublication(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanGetLibrary() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canGetLibrary(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCannotGetLibrary() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(false);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canGetLibrary(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userWithNoRoleCannotGetLibrary() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canGetLibrary(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanAddToLibrary() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canAddToLibrary(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCannotAddToLibrary() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(false);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canAddToLibrary(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userWithNoRoleCannotAddToLibrary() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canAddToLibrary(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanSeeList() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canSeeList(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanSeeList() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canSeeList(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotSeeList() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canSeeList(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanAddItemToList() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canAddItemTo(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanAddItemToList() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canAddItemTo(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotAddItemToList() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canAddItemTo(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanDeleteList() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canDeleteList(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanDeleteList() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canDeleteList(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotDeleteList() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canDeleteList(user);

        // Assert
        assertFalse(result);
    }
    // ------------------------------------------------------------------------
    // Author Aggregate Authorization Tests
    // ------------------------------------------------------------------------

    @Test
    void userCanListAuthors() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canListAuthors(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanListAuthors() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canListAuthors(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotListAuthors() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canListAuthors(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanCreateAuthor() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        // Act
        boolean result = policy.canCreateAuthor(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanCreateAuthor() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        // Act
        boolean result = policy.canCreateAuthor(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotCreateAuthor() {
        // Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        // Act
        boolean result = policy.canCreateAuthor(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void guestCannotFilterDirectSales() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        //Act & Assert
        assertFalse(policy.canFilterDirectSales(user));
    }

    @Test
    void userCanCreateItem() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        //Act & Assert
        assertTrue(policy.canCreateItem(user));
    }

    @Test
    void adminCanCreateItem() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        //Act & Assert
        assertTrue(policy.canCreateItem(user));
    }

    @Test
    void guestCannotCreateItem() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        //Act & Assert
        assertFalse(policy.canCreateItem(user));
    }

    @Test
    void userCanListItems() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.USER)).thenReturn(true);

        //Act & Assert
        assertTrue(policy.canListItems(user));
    }

    @Test
    void adminCanListItems() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);

        //Act & Assert
        assertTrue(policy.canListItems(user));
    }

    @Test
    void guestCannotListItems() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(any())).thenReturn(false);

        //Act & Assert
        assertFalse(policy.canListItems(user));
    }

    @Test
    void userCanSeePrice() {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.NONREGISTRED)).thenReturn(false);

        //Act & Assert
        assertFalse(policy.cannotSeePrice(user));
    }

    @Test
    void adminCanSeePrice() {
        User user = mock(User.class);
        when(user.hasRole(Role.NONREGISTRED)).thenReturn(false);

        //Act & Assert
        assertFalse(policy.cannotSeePrice(user));
    }
}