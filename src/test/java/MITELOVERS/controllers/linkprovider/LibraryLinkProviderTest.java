package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryLinkProviderTest {

    @Test
    void userWithAllPermissionsGetsBothLinks() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canGetLibrary(_userDouble)).thenReturn(true);
        when(_policyDouble.canAddToLibrary(_userDouble)).thenReturn(true);

        // SUT
        LibraryLinkProvider provider = new LibraryLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertEquals(2, links.size());
    }

    @Test
    void userWithNoPermissionsGetsNoLinks() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canGetLibrary(_userDouble)).thenReturn(false);
        when(_policyDouble.canAddToLibrary(_userDouble)).thenReturn(false);

        // SUT
        LibraryLinkProvider provider = new LibraryLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void userWithOnlyGetLibraryPermissionGetsOneLink() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);

        when(_policyDouble.canGetLibrary(_userDouble)).thenReturn(true);
        when(_policyDouble.canAddToLibrary(_userDouble)).thenReturn(false);

        // SUT
        LibraryLinkProvider provider = new LibraryLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertEquals(1, links.size());

        Link libraryLink = links.getFirst();

        assertEquals("library", libraryLink.getRel().value());
        assertEquals("/my-library", libraryLink.getHref());
        assertFalse(libraryLink.isTemplated());
    }

    @Test
    void userWithOnlyAddToLibraryPermissionGetsOneLink() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canGetLibrary(_userDouble)).thenReturn(false);
        when(_policyDouble.canAddToLibrary(_userDouble)).thenReturn(true);

        // SUT
        LibraryLinkProvider provider = new LibraryLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("library-add")));
    }

    @Test
    void linksContainCorrectRels() {
        // Arrange
        AuthorizationPolicy _policyDouble = mock(AuthorizationPolicy.class);
        User _userDouble = mock(User.class);
        when(_policyDouble.canGetLibrary(_userDouble)).thenReturn(true);
        when(_policyDouble.canAddToLibrary(_userDouble)).thenReturn(true);

        // SUT
        LibraryLinkProvider provider = new LibraryLinkProvider(_policyDouble);
        List<Link> links = provider.getLinks(_userDouble);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("library")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("library-add")));
    }
}