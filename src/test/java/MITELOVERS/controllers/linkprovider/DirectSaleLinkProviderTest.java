package MITELOVERS.controllers.linkprovider;

import MITELOVERS.applicationservices.UserService;
import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.DirectSaleStatus;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import MITELOVERS.dto.response.DirectSaleNoPriceResponseDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectSaleLinkProviderTest {

    @Mock
    private AuthorizationPolicy authorizationPolicyDouble;

    @Mock
    private UserService userServiceDouble;

    @InjectMocks
    private DirectSaleLinkProvider provider;

    @Mock
    private User userDouble;

    @Test
    void shouldIncludeListDirectSalesLinkWhenAuthorized() {

        // Arrange
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("direct-sales", links.get(0).getRel().value());
    }

    @Test
    void shouldIncludeListActiveDirectSalesLinkWhenAuthorized() {

        // Arrange
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canListActiveDirectSales(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("active-direct-sales", links.get(0).getRel().value());
    }

    @Test
    void shouldNotIncludeListDirectSalesLinkWhenUnauthorized() {

        // Arrange
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldIncludeCreateDirectSaleLinkWhenAuthorized() {

        // Arrange
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("create-direct-sale", links.get(0).getRel().value());
    }

    @Test
    void shouldNotIncludeCreateDirectSaleLinkWhenUnauthorized() {

        // Arrange
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldIncludeFilterDirectSalesLinkWhenAuthorized() {

        // Arrange
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(true);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("direct-sales-by-genre", links.get(0).getRel().value());
    }

    @Test
    void shouldNotIncludeFilterDirectSalesLinkWhenUnauthorized() {

        // Arrange
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldIncludeAllLinksWhenAllPermissionsGranted() {

        // Arrange
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canGetDirectSale(userDouble)).thenReturn(true);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sales")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-direct-sale")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sales-by-genre")));
        assertEquals(4, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sale")));
    }

    @Test
    void shouldIncludeOnlyNonRegisteredPermissionsWhenUserIsNotAuthorized() {

        // Arrange
        when(authorizationPolicyDouble.cannotSeePrice(any(User.class))).thenReturn(true);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sales-without-price")));
    }

    @Test
    void shouldIncludeGetDirectSaleByIdLinkWhenAuthorized() {

        // Arrange
        when(authorizationPolicyDouble.canGetDirectSale(userDouble)).thenReturn(true);

        // Act
        List<Link> links = provider.getLinks(userDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("direct-sale", links.get(0).getRel().value());
    }

    @Test
    void addResourceLinks_shouldAddSelfLinkOnly_whenUserCannotDelete() {

        // Arrange
        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                "DS-ABCDEF12",
                List.of("ABC123DEF0"),
                10.0,
                "EUR",
                3600L,
                Instant.parse("2024-01-01T10:00:00Z"),
                null,
                DirectSaleStatus.ACTIVE,
                "pedro@aeiou.com"
        );

        String email = "user@email.com";

        when(userServiceDouble.getUserByEmail(email)).thenReturn(userDouble);
        when(authorizationPolicyDouble.canDeleteList(userDouble)).thenReturn(false);

        // Act
        provider.addResourceLinks(dto, email);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertFalse(dto.hasLink("delete"));
    }

    @Test
    void addResourceLinks_shouldAddSelfAndDeleteLinks_whenUserCanDelete() {

        // Arrange
        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                "DS-ABCDEF12",
                List.of("ABC123DEF0"),
                10.0,
                "EUR",
                3600L,
                Instant.parse("2024-01-01T10:00:00Z"),
                null,
                DirectSaleStatus.ACTIVE,
                "pedro@aeiou.com"
        );

        String email = "admin@email.com";

        when(userServiceDouble.getUserByEmail(email)).thenReturn(userDouble);
        when(authorizationPolicyDouble.canDeleteList(userDouble)).thenReturn(true);

        // Act
        provider.addResourceLinks(dto, email);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.hasLink("delete"));
    }

    @Test
    void addCollectionLinks_shouldAddSelfLink() {

        // Arrange
        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                "DS-ABCDEF12",
                List.of("ABC123DEF0"),
                10.0,
                "EUR",
                3600L,
                Instant.parse("2024-01-01T10:00:00Z"),
                null,
                DirectSaleStatus.ACTIVE,
                "pedro@aeiou.com"
        );

        CollectionModel<DirectSaleResponseDTO> dtos = CollectionModel.of(List.of(dto));

        // Act
        provider.addCollectionLinks(dtos, "user@email.com");

        // Assert
        assertTrue(dtos.hasLink("self"));
    }

    @Test
    void addResourceLinks_shouldAddSelfLinkToDirectSaleResponse() {

        // Arrange
        DirectSaleResponseDTO dto = new DirectSaleResponseDTO(
                "DS-ABCDEF12",
                List.of("ABC123DEF0"),
                10.0,
                "EUR",
                3600L,
                Instant.parse("2024-01-01T10:00:00Z"),
                null,
                DirectSaleStatus.ACTIVE,
                "pedro@aeiou.com"
        );

        // Act
        provider.addResourceLinks(dto);

        // Assert
        assertTrue(dto.hasLink("self"));
    }

    @Test
    void addResourceLinks_shouldAddSelfLinkToNoPriceResponse() {

        // Arrange
        DirectSaleNoPriceResponseDTO dto = new DirectSaleNoPriceResponseDTO(
                "DS-ABCDEF12",
                List.of("ABC123DEF0"),
                3600L,
                Instant.parse("2024-01-01T10:00:00Z")
        );

        // Act
        provider.addResourceLinks(dto);

        // Assert
        assertTrue(dto.hasLink("self"));
    }

    @Test
    void addCollectionLinks_shouldAddSelfLinkToFilteredCollectionAndEntries() {

        // Arrange
        String genreId = "FICTION";
        DSFilteredItemsResponseDTO dto =
                new DSFilteredItemsResponseDTO(List.of(
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-ABCDEF12"),
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-1234ABCD")
                ));

        // Act
        provider.addCollectionLinks(dto, genreId);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getDirectSales().get(0).hasLink("self"));
        assertTrue(dto.getDirectSales().get(1).hasLink("self"));
    }

}