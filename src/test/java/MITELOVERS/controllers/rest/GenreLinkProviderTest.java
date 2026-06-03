package MITELOVERS.controllers.rest;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreLinkProviderTest {

    @Test
    void userWithAllPermissionsGetsBothLinks() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListGenres(_userDouble)).thenReturn(true);
        when(_policyDouble.canAddGenre(_userDouble)).thenReturn(true);

        // SUT
        GenreLinkProvider provider = new GenreLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertEquals(2, links.size());
    }

    @Test
    void userWithNoPermissionsGetsNoLinks() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListGenres(_userDouble)).thenReturn(false);
        when(_policyDouble.canAddGenre(_userDouble)).thenReturn(false);

        // SUT
        GenreLinkProvider provider = new GenreLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void userWithOnlyListGenresPermissionGetsOneLink() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListGenres(_userDouble)).thenReturn(true);
        when(_policyDouble.canAddGenre(_userDouble)).thenReturn(false);

        // SUT
        GenreLinkProvider provider = new GenreLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("genres")));
    }

    @Test
    void userWithOnlyAddGenrePermissionGetsOneLink() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListGenres(_userDouble)).thenReturn(false);
        when(_policyDouble.canAddGenre(_userDouble)).thenReturn(true);

        // SUT
        GenreLinkProvider provider = new GenreLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-genre")));
    }

    @Test
    void linksContainCorrectRels() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListGenres(_userDouble)).thenReturn(true);
        when(_policyDouble.canAddGenre(_userDouble)).thenReturn(true);

        // SUT
        GenreLinkProvider provider = new GenreLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("genres")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-genre")));
    }
}

