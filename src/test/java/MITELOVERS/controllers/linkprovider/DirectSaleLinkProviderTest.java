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
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        List<Link> links = provider.getLinks(userDouble);

        assertEquals(1, links.size());
        assertEquals("direct-sales", links.get(0).getRel().value());
    }

    @Test
    void shouldIncludeListActiveDirectSalesLinkWhenAuthorized() {
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canListActiveDirectSales(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        List<Link> links = provider.getLinks(userDouble);

        assertEquals(1, links.size());
        assertEquals("active-direct-sales", links.get(0).getRel().value());
    }

    @Test
    void shouldNotIncludeListDirectSalesLinkWhenUnauthorized() {
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        List<Link> links = provider.getLinks(userDouble);

        assertTrue(links.isEmpty());
    }

    @Test
    void shouldIncludeCreateDirectSaleLinkWhenAuthorized() {
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        List<Link> links = provider.getLinks(userDouble);

        assertEquals(1, links.size());
        assertEquals("create-direct-sale", links.get(0).getRel().value());
    }

    @Test
    void shouldNotIncludeCreateDirectSaleLinkWhenUnauthorized() {
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        List<Link> links = provider.getLinks(userDouble);

        assertTrue(links.isEmpty());
    }

    @Test
    void shouldIncludeFilterDirectSalesLinkWhenAuthorized() {
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(true);

        List<Link> links = provider.getLinks(userDouble);

        assertEquals(1, links.size());
        assertEquals("direct-sales-by-genre", links.get(0).getRel().value());
    }

    @Test
    void shouldNotIncludeFilterDirectSalesLinkWhenUnauthorized() {
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(false);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(false);

        List<Link> links = provider.getLinks(userDouble);

        assertTrue(links.isEmpty());
    }

    @Test
    void shouldIncludeAllLinksWhenAllPermissionsGranted() {
        when(authorizationPolicyDouble.canListDirectSales(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canCreateDirectSale(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canFilterDirectSales(userDouble)).thenReturn(true);
        when(authorizationPolicyDouble.canGetDirectSale(userDouble)).thenReturn(true);

        List<Link> links = provider.getLinks(userDouble);

        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sales")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-direct-sale")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sales-by-genre")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sale")));
        assertEquals(4, links.size());
    }

    @Test
    void shouldIncludeOnlyNonRegisteredPermissionsWhenUserIsNotAuthorized() {
        when(authorizationPolicyDouble.cannotSeePrice(any(User.class))).thenReturn(true);

        List<Link> links = provider.getLinks(userDouble);

        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("direct-sales-without-price")));
    }

    @Test
    void shouldIncludeGetDirectSaleByIdLinkWhenAuthorized() {
        when(authorizationPolicyDouble.canGetDirectSale(userDouble)).thenReturn(true);

        List<Link> links = provider.getLinks(userDouble);

        assertEquals(1, links.size());
        assertEquals("direct-sale", links.get(0).getRel().value());
    }

    @Test
    void addResourceLinks_shouldAddSelfLinkOnly_whenUserCannotDelete() {
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

        when(userServiceDouble.getUserByEmail(new MITELOVERS.domain.valueobject.UserId(new MITELOVERS.domain.valueobject.Email(email)))).thenReturn(userDouble);
        when(authorizationPolicyDouble.canDeleteList(userDouble)).thenReturn(false);

        provider.addResourceLinks(dto, email);

        assertTrue(dto.hasLink("self"));
        assertFalse(dto.hasLink("delete"));
    }

    @Test
    void addResourceLinks_shouldAddSelfAndDeleteLinks_whenUserCanDelete() {
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

        when(userServiceDouble.getUserByEmail(new MITELOVERS.domain.valueobject.UserId(new MITELOVERS.domain.valueobject.Email(email)))).thenReturn(userDouble);
        when(authorizationPolicyDouble.canDeleteList(userDouble)).thenReturn(true);

        provider.addResourceLinks(dto, email);

        assertTrue(dto.hasLink("self"));
        assertTrue(dto.hasLink("delete"));
    }

    @Test
    void addCollectionLinks_shouldAddSelfLink() {
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

        provider.addCollectionLinks(dtos, "user@email.com");

        assertTrue(dtos.hasLink("self"));
    }

    @Test
    void addResourceLinks_shouldAddSelfLinkToDirectSaleResponse() {
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

        provider.addResourceLinks(dto);

        assertTrue(dto.hasLink("self"));
    }

    @Test
    void addResourceLinks_shouldAddSelfLinkToNoPriceResponse() {
        DirectSaleNoPriceResponseDTO dto = new DirectSaleNoPriceResponseDTO(
                "DS-ABCDEF12",
                List.of("ABC123DEF0"),
                3600L,
                Instant.parse("2024-01-01T10:00:00Z")
        );

        provider.addResourceLinks(dto);

        assertTrue(dto.hasLink("self"));
    }

    @Test
    void addCollectionLinks_shouldAddSelfLinkToFilteredCollectionAndEntries() {
        String genreId = "FICTION";
        DSFilteredItemsResponseDTO dto =
                new DSFilteredItemsResponseDTO(List.of(
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-ABCDEF12"),
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-1234ABCD")
                ));

        provider.addCollectionLinks(dto, genreId);

        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getDirectSales().get(0).hasLink("self"));
        assertTrue(dto.getDirectSales().get(1).hasLink("self"));
    }
}