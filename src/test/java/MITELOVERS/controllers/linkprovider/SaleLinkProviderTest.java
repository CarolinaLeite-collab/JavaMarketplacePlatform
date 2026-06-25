package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.SaleLineId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.SaleLineResponseDTO;
import MITELOVERS.dto.response.SaleResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleLinkProviderTest {

    @Mock
    private AuthorizationPolicy _authorizationPolicy;

    @InjectMocks
    private SaleLinkProvider _linkProvider;

    @Test
    void getAllowedMethodsForSalesReturnsGetAndPostAndOptionsWhenAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canGetSales(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canPostSales(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForSales(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForSalesReturnsOnlyGetAndOptionsWhenCannotPost() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canGetSales(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canPostSales(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForSales(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.GET, HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForSalesReturnsOnlyOptionsWhenNotAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canGetSales(userDouble)).thenReturn(false);
        when(_authorizationPolicy.canPostSales(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForSales(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForSaleReturnsGetAndOptionsWhenOwnerAndAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canGetSale(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForSale(userDouble, saleDouble);

        // Assert
        assertEquals(List.of(HttpMethod.GET, HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForSaleReturnsOnlyOptionsWhenOwnerButNotAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canGetSale(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForSale(userDouble, saleDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForSaleReturnsOnlyOptionsWhenNotOwner() {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(mock(UserId.class));

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForSale(userDouble, saleDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForSaleLineReturnsGetAndOptionsWhenOwnerAndAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canGetSale(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForSaleLine(userDouble, saleDouble);

        // Assert
        assertEquals(List.of(HttpMethod.GET, HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForSaleLineReturnsOnlyOptionsWhenOwnerButNotAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canGetSale(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForSaleLine(userDouble, saleDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForSaleLineReturnsOnlyOptionsWhenNotOwner() {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(mock(UserId.class));

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForSaleLine(userDouble, saleDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void addLinksForSalesAddsSaleLinkPerSale() {
        // Arrange
        SaleId saleId1 = new SaleId("SA-1234ABCD");
        SaleId saleId2 = new SaleId("SA-5678EFGH");

        Sale sale1Double = mock(Sale.class);
        when(sale1Double.identity()).thenReturn(saleId1);

        Sale sale2Double = mock(Sale.class);
        when(sale2Double.identity()).thenReturn(saleId2);

        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.toString()).thenReturn("pedro@aeiou.com");

        RepresentationModel<?> model = new RepresentationModel<>();

        // Act
        _linkProvider.addLinksForSales(model, userIdDouble, List.of(sale1Double, sale2Double));

        // Assert
        assertTrue(model.hasLink("sale"));
        List<Link> saleLinks = model.getLinks("sale").stream().toList();
        assertEquals(2, saleLinks.size());
        assertTrue(saleLinks.stream().anyMatch(l -> l.getHref().endsWith("/sales/SA-1234ABCD")));
        assertTrue(saleLinks.stream().anyMatch(l -> l.getHref().endsWith("/sales/SA-5678EFGH")));
    }

    @Test
    void addLinksForSalesAddsNoLinksWhenSalesEmpty() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        RepresentationModel<?> model = new RepresentationModel<>();

        // Act
        _linkProvider.addLinksForSales(model, userIdDouble, List.of());

        // Assert
        assertFalse(model.hasLink("sale"));
    }

    @Test
    void addLinksForSaleAddsSelfLink() {
        // Arrange
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");
        SaleId saleId = new SaleId("SA-1234ABCD");

        SaleLine saleLineDouble = mock(SaleLine.class);
        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_saleLines()).thenReturn(List.of(saleLineDouble));

        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.toString()).thenReturn("pedro@aeiou.com");

        SaleResponseDTO dto =
                new SaleResponseDTO(
                "SA-1234ABCD", "pedro@aeiou.com", 29.99,
                "EUR", "2026-06-18T10:00:00", null, List.of()
        );
        // Act
        _linkProvider.addLinksForSale(dto, userIdDouble, saleId, saleDouble);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getRequiredLink("self").getHref().endsWith("/sales/SA-1234ABCD"));
    }

    @Test
    void addLinksForSaleAddsSaleLineLink() {
        // Arrange
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");
        SaleId saleId = new SaleId("SA-1234ABCD");

        SaleLine saleLineDouble = mock(SaleLine.class);
        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_saleLines()).thenReturn(List.of(saleLineDouble));

        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.toString()).thenReturn("pedro@aeiou.com");

        SaleResponseDTO dto = new SaleResponseDTO(
                "SA-1234ABCD", "pedro@aeiou.com", 29.99,
                "EUR", "2026-06-18T10:00:00", null, List.of()
        );

        // Act
        _linkProvider.addLinksForSale(dto, userIdDouble, saleId, saleDouble);

        // Assert
        assertTrue(dto.hasLink("sale-line"));
        assertTrue(dto.getRequiredLink("sale-line").getHref()
                .endsWith("/sales/SA-1234ABCD/sale-lines/SL-1234ABCD"));
    }

    @Test
    void addLinksForSaleLineAddsSelfLink() {
        // Arrange
        SaleId saleId = new SaleId("SA-1234ABCD");
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.toString()).thenReturn("pedro@aeiou.com");

        SaleLineResponseDTO dto = new SaleLineResponseDTO(
                "SL-1234ABCD", "ana@aeiou.com", "DS-1A2B3C4DE", 14.99, "EUR");

        // Act
        _linkProvider.addLinksForSaleLine(dto, userIdDouble, saleId, saleLineId);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getRequiredLink("self").getHref()
                .endsWith("/sales/SA-1234ABCD/sale-lines/SL-1234ABCD"));
    }

    @Test
    void addLinksForSaleLineAddsSaleLink() {
        // Arrange
        SaleId saleId = new SaleId("SA-1234ABCD");
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.toString()).thenReturn("pedro@aeiou.com");

        SaleLineResponseDTO dto = new SaleLineResponseDTO(
                "SL-1234ABCD", "ana@aeiou.com", "DS-1A2B3C4DE", 14.99, "EUR");

        // Act
        _linkProvider.addLinksForSaleLine(dto, userIdDouble, saleId, saleLineId);

        // Assert
        assertTrue(dto.hasLink("sale"));
        assertTrue(dto.getRequiredLink("sale").getHref().endsWith("/sales/SA-1234ABCD"));
    }

    @Test
    void addLinksForCreatedSaleAddsSelfLink() {
        // Arrange
        SaleId saleId = new SaleId("SA-1234ABCD");

        UserId userIdDouble = mock(UserId.class);
        Email emailDouble = mock(Email.class);
        when(userIdDouble.getEmail()).thenReturn(emailDouble);
        when(emailDouble.toString()).thenReturn("pedro@aeiou.com");

        RepresentationModel<?> model = new RepresentationModel<>();

        // Act
        _linkProvider.addLinksForCreatedSale(model, userIdDouble, saleId);

        // Assert
        assertTrue(model.hasLink("self"));
        assertTrue(model.getRequiredLink("self").getHref().endsWith("/sales/SA-1234ABCD"));
    }

    @Test
    void getLinksReturnsSalesLinkWhenAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canGetSales(userDouble)).thenReturn(true);

        // Act
        List<Link> result = _linkProvider.getLinks(userDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("sales", result.get(0).getRel().value());
        assertTrue(result.get(0).getHref().endsWith("/sales"));
    }

    @Test
    void getLinksReturnsEmptyListWhenNotAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canGetSales(userDouble)).thenReturn(false);

        // Act
        List<Link> result = _linkProvider.getLinks(userDouble);

        // Assert
        assertTrue(result.isEmpty());
    }
}