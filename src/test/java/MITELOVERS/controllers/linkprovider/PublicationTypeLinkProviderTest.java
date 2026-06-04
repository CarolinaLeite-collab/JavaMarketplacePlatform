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

class PublicationTypeLinkProviderTest {

    @Test
    void userWithPermissionGetsListLink() {
        // Arrange
        AuthorizationPolicy policyDouble = mock(AuthorizationPolicy.class);
        User userDouble = mock(User.class);
        when(policyDouble.canListPublicationTypes(userDouble)).thenReturn(true);

        // SUT
        PublicationTypeLinkProvider provider = new PublicationTypeLinkProvider(policyDouble);
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertEquals(1, links.size());
    }

    @Test
    void userWithoutPermissionGetsNoLinks() {
        // Arrange
        AuthorizationPolicy policyDouble = mock(AuthorizationPolicy.class);
        User userDouble = mock(User.class);
        when(policyDouble.canListPublicationTypes(userDouble)).thenReturn(false);

        // SUT
        PublicationTypeLinkProvider provider = new PublicationTypeLinkProvider(policyDouble);
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void linkContainsCorrectRel() {
        // Arrange
        AuthorizationPolicy policyDouble = mock(AuthorizationPolicy.class);
        User userDouble = mock(User.class);
        when(policyDouble.canListPublicationTypes(userDouble)).thenReturn(true);

        // SUT
        PublicationTypeLinkProvider provider = new PublicationTypeLinkProvider(policyDouble);
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("publication-types")));
    }
}