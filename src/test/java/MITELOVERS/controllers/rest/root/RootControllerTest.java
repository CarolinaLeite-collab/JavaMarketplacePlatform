package MITELOVERS.controllers.rest.root;

import MITELOVERS.applicationservices.UserService;
import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.AuthorLinkProvider;
import MITELOVERS.controllers.rest.GenreLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Role;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RootControllerTest {

    private final UserService _userServiceDouble = mock(UserService.class);
    private final RootLinkProvider _linkProviderDouble = mock(RootLinkProvider.class);

    private final RootController controller =
            new RootController(List.of(_linkProviderDouble), _userServiceDouble);

    @Test
    void shouldReturnRootWithSelfLinkAndProviderLinks() {
        // Arrange
        String email = "test@email.com";
        User user = mock(User.class);

        when(_userServiceDouble.getUserByEmail(email)).thenReturn(user);

        Link myListsLink = Link.of("/api/my-lists").withRel("myLists");

        when(_linkProviderDouble.getLinks(user))
                .thenReturn(List.of(myListsLink));

        // Act
        RepresentationModel<?> result = controller.root(email);

        // Assert
        assertTrue(result.hasLink("self"));
        assertTrue(result.hasLink("myLists"));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        String email = "missing@email.com";

        when(_userServiceDouble.getUserByEmail(email))
                .thenThrow(new NoSuchElementException("User not found: " + email));

        // Act + Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> controller.root(email)
        );

        assertEquals("User not found: " + email, exception.getMessage());
    }

    @Test
    void shouldReturnRootWithAuthorAndGenreLinksForUserRole() {
        // Arrange
        String email = "pedro@aeiou.com";
        User user = mock(User.class);

        when(user.hasRole(Role.USER)).thenReturn(true);
        when(user.hasRole(Role.ADMIN)).thenReturn(false);
        when(_userServiceDouble.getUserByEmail(email)).thenReturn(user);

        AuthorizationPolicy authorizationPolicy = new AuthorizationPolicy();
        RootController roleAwareController = new RootController(
                List.of(
                        new GenreLinkProvider(authorizationPolicy),
                        new AuthorLinkProvider(authorizationPolicy)
                ),
                _userServiceDouble
        );

        // Act
        RepresentationModel<?> result = roleAwareController.root(email);

        // Assert
        assertTrue(result.hasLink("self"));
        assertTrue(result.hasLink("genres"));
        assertFalse(result.hasLink("create-genre"));
        assertTrue(result.hasLink("authors"));
        assertTrue(result.hasLink("create-author"));
    }

    @Test
    void shouldReturnRootWithAuthorAndGenreLinksForAdminRole() {
        // Arrange
        String email = "admin@email.com";
        User user = mock(User.class);

        when(user.hasRole(Role.USER)).thenReturn(true);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);
        when(_userServiceDouble.getUserByEmail(email)).thenReturn(user);

        AuthorizationPolicy authorizationPolicy = new AuthorizationPolicy();
        RootController roleAwareController = new RootController(
                List.of(
                        new GenreLinkProvider(authorizationPolicy),
                        new AuthorLinkProvider(authorizationPolicy)
                ),
                _userServiceDouble
        );

        // Act
        RepresentationModel<?> result = roleAwareController.root(email);

        // Assert
        assertTrue(result.hasLink("self"));
        assertTrue(result.hasLink("genres"));
        assertTrue(result.hasLink("create-genre"));
        assertTrue(result.hasLink("authors"));
        assertTrue(result.hasLink("create-author"));
    }

    @Test
    void shouldNotReturnAuthorAndGenreLinksWhenUserHasNoRoles() {
        // Arrange
        String email = "guest@email.com";
        User user = mock(User.class);

        when(user.hasRole(Role.USER)).thenReturn(false);
        when(user.hasRole(Role.ADMIN)).thenReturn(false);
        when(_userServiceDouble.getUserByEmail(email)).thenReturn(user);

        AuthorizationPolicy authorizationPolicy = new AuthorizationPolicy();
        RootController roleAwareController = new RootController(
                List.of(
                        new GenreLinkProvider(authorizationPolicy),
                        new AuthorLinkProvider(authorizationPolicy)
                ),
                _userServiceDouble
        );

        // Act
        RepresentationModel<?> result = roleAwareController.root(email);

        // Assert
        assertTrue(result.hasLink("self"));
        assertFalse(result.hasLink("genres"));
        assertFalse(result.hasLink("create-genre"));
        assertFalse(result.hasLink("authors"));
        assertFalse(result.hasLink("create-author"));
    }
}