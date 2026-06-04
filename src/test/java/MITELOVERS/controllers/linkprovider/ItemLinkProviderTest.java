package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemLinkProviderTest {

    AuthorizationPolicy policyDouble;
    User userDouble;
    ItemLinkProvider linkProvider;

    @BeforeEach
    void setUp() {
        policyDouble  = mock(AuthorizationPolicy.class);
        userDouble    = mock(User.class);
        linkProvider  = new ItemLinkProvider(policyDouble);
    }

    @Test
    void getLinksWithAllPermissionsReturnsThreeLinks() {
        when(policyDouble.canCreateItem(userDouble)).thenReturn(true);
        when(policyDouble.canListItems(userDouble)).thenReturn(true);
        when(policyDouble.canGetLibrary(userDouble)).thenReturn(true);

        List<Link> links = linkProvider.getLinks(userDouble);

        assertEquals(3, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("createItem")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("items")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("myLibraryItems")));
    }

    @Test
    void getLinksWithNoPermissionsReturnsEmptyList() {
        when(policyDouble.canCreateItem(userDouble)).thenReturn(false);
        when(policyDouble.canListItems(userDouble)).thenReturn(false);
        when(policyDouble.canGetLibrary(userDouble)).thenReturn(false);

        List<Link> links = linkProvider.getLinks(userDouble);

        assertTrue(links.isEmpty());
    }

    @Test
    void getLinksWithOnlyCanCreateItemReturnsCreateItemLink() {
        when(policyDouble.canCreateItem(userDouble)).thenReturn(true);
        when(policyDouble.canListItems(userDouble)).thenReturn(false);
        when(policyDouble.canGetLibrary(userDouble)).thenReturn(false);

        List<Link> links = linkProvider.getLinks(userDouble);

        assertEquals(1, links.size());
        assertEquals("createItem", links.get(0).getRel().value());
    }

    @Test
    void getLinksWithOnlyCanListItemsReturnsItemsLink() {
        when(policyDouble.canCreateItem(userDouble)).thenReturn(false);
        when(policyDouble.canListItems(userDouble)).thenReturn(true);
        when(policyDouble.canGetLibrary(userDouble)).thenReturn(false);

        List<Link> links = linkProvider.getLinks(userDouble);

        assertEquals(1, links.size());
        assertEquals("items", links.get(0).getRel().value());
    }

    @Test
    void getLinksWithOnlyCanGetLibraryReturnsMyLibraryItemsLink() {
        when(policyDouble.canCreateItem(userDouble)).thenReturn(false);
        when(policyDouble.canListItems(userDouble)).thenReturn(false);
        when(policyDouble.canGetLibrary(userDouble)).thenReturn(true);

        List<Link> links = linkProvider.getLinks(userDouble);

        assertEquals(1, links.size());
        assertEquals("myLibraryItems", links.get(0).getRel().value());
    }
}