package MITELOVERS.controllers.rest;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectSaleLinkProviderTest {

    @Mock
    private AuthorizationPolicy authorizationPolicy;

    @InjectMocks
    private DirectSaleLinkProvider provider;

    @Mock
    private User user;

    @Test
    void shouldIncludeListDirectSalesLinkWhenAuthorized() {

        // Arrange
        when(authorizationPolicy.canListDirectSales(user)).thenReturn(true);
        when(authorizationPolicy.canCreateDirectSale(user)).thenReturn(false);
        when(authorizationPolicy.canFilterDirectSales(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(user);

        // Assert
        assertEquals(1, links.size());
        assertEquals("direct-sales", links.get(0).getRel().value());
    }

    @Test
    void shouldNotIncludeListDirectSalesLinkWhenUnauthorized() {

        // Arrange
        when(authorizationPolicy.canListDirectSales(user)).thenReturn(false);
        when(authorizationPolicy.canCreateDirectSale(user)).thenReturn(false);
        when(authorizationPolicy.canFilterDirectSales(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(user);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldIncludeCreateDirectSaleLinkWhenAuthorized() {

        // Arrange
        when(authorizationPolicy.canListDirectSales(user)).thenReturn(false);
        when(authorizationPolicy.canCreateDirectSale(user)).thenReturn(true);
        when(authorizationPolicy.canFilterDirectSales(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(user);

        // Assert
        assertEquals(1, links.size());
        assertEquals("create-direct-sale", links.get(0).getRel().value());
    }

    @Test
    void shouldNotIncludeCreateDirectSaleLinkWhenUnauthorized() {

        // Arrange
        when(authorizationPolicy.canListDirectSales(user)).thenReturn(false);
        when(authorizationPolicy.canCreateDirectSale(user)).thenReturn(false);
        when(authorizationPolicy.canFilterDirectSales(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(user);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldIncludeFilterDirectSalesLinkWhenAuthorized() {

        // Arrange
        when(authorizationPolicy.canListDirectSales(user)).thenReturn(false);
        when(authorizationPolicy.canCreateDirectSale(user)).thenReturn(false);
        when(authorizationPolicy.canFilterDirectSales(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLinks(user);

        // Assert
        assertEquals(1, links.size());
        assertEquals("direct-sales-by-genre", links.get(0).getRel().value());
    }

    @Test
    void shouldNotIncludeFilterDirectSalesLinkWhenUnauthorized() {

        // Arrange
        when(authorizationPolicy.canListDirectSales(user)).thenReturn(false);
        when(authorizationPolicy.canCreateDirectSale(user)).thenReturn(false);
        when(authorizationPolicy.canFilterDirectSales(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(user);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldIncludeAllLinksWhenAllPermissionsGranted() {

        // Arrange
        when(authorizationPolicy.canListDirectSales(user)).thenReturn(true);
        when(authorizationPolicy.canCreateDirectSale(user)).thenReturn(true);
        when(authorizationPolicy.canFilterDirectSales(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLinks(user);

        // Assert
        assertEquals(3, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sales")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-direct-sale")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sales-by-genre")));
    }

}