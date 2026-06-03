package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EditionLinkProviderTest {

    @Test
    void userWithAllPermissionsGetsBothLinks() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListEditions(_userDouble)).thenReturn(true);
        when(_policyDouble.canCreateEdition(_userDouble)).thenReturn(true);

        // SUT
        EditionLinkProvider provider = new EditionLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertEquals(2, links.size());
    }

    @Test
    void userWithNoPermissionsGetsNoLinks() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListEditions(_userDouble)).thenReturn(false);
        when(_policyDouble.canCreateEdition(_userDouble)).thenReturn(false);

        // SUT
        EditionLinkProvider provider = new EditionLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void userWithOnlyListPermissionGetsOneLink() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListEditions(_userDouble)).thenReturn(true);
        when(_policyDouble.canCreateEdition(_userDouble)).thenReturn(false);

        // SUT
        EditionLinkProvider provider = new EditionLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("editions")));
    }

    @Test
    void userWithOnlyCreatePermissionGetsOneLink() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListEditions(_userDouble)).thenReturn(false);
        when(_policyDouble.canCreateEdition(_userDouble)).thenReturn(true);

        // SUT
        EditionLinkProvider provider = new EditionLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("edition-create")));
    }

    @Test
    void linksContainCorrectRels() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canListEditions(_userDouble)).thenReturn(true);
        when(_policyDouble.canCreateEdition(_userDouble)).thenReturn(true);

        // SUT
        EditionLinkProvider provider = new EditionLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("editions")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("edition-create")));
    }
}