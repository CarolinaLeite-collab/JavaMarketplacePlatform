package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.user.User;
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
import static org.mockito.Mockito.*;

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
        verifyNoInteractions(_authorizationPolicy);
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
        verifyNoInteractions(_authorizationPolicy);
    }

    // ──────────── addLinksForSales ────────────

    @Test
    void addLinksForSalesAddsSaleLinkPerSale() {
        // Arrange
        SaleId saleId1 = new SaleId("SA-1234ABCD");
        SaleId saleId2 = new SaleId("SA-5678EFGH");

        Sale sale1Double = mock(Sale.class);
        when(sale1Double.identity()).thenReturn(saleId1);

        Sale sale2Double = mock(Sale.class);
        when(sale2Double.identity()).thenReturn(saleId2);

        RepresentationModel<?> model = new RepresentationModel<>();

        // Act
        _linkProvider.addLinksForSales(model, "pedro@aeiou.com", List.of(sale1Double, sale2Double));

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
        RepresentationModel<?> model = new RepresentationModel<>();

        // Act
        _linkProvider.addLinksForSales(model, "pedro@aeiou.com", List.of());

        // Assert
        assertFalse(model.hasLink("sale"));
    }

    @Test
    void addLinksForSaleAddsSelfLink() {
        // Arrange
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        SaleLine saleLineDouble = mock(SaleLine.class);
        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_saleLines()).thenReturn(List.of(saleLineDouble));

        SaleResponseDTO dto = new SaleResponseDTO(
                "SA-1234ABCD", "pedro@aeiou.com", 29.99, "EUR", "2026-06-18T10:00:00", null);

        // Act
        _linkProvider.addLinksForSale(dto, "pedro@aeiou.com", "SA-1234ABCD", saleDouble);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getRequiredLink("self").getHref().endsWith("/sales/SA-1234ABCD"));
    }

    @Test
    void addLinksForSaleAddsSaleLineLink() {
        // Arrange
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        SaleLine saleLineDouble = mock(SaleLine.class);
        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_saleLines()).thenReturn(List.of(saleLineDouble));

        SaleResponseDTO dto = new SaleResponseDTO(
                "SA-1234ABCD", "pedro@aeiou.com", 29.99, "EUR", "2026-06-18T10:00:00", null);

        // Act
        _linkProvider.addLinksForSale(dto, "pedro@aeiou.com", "SA-1234ABCD", saleDouble);

        // Assert
        assertTrue(dto.hasLink("sale-line"));
        assertTrue(dto.getRequiredLink("sale-line").getHref()
                .endsWith("/sales/SA-1234ABCD/sale-lines/SL-1234ABCD"));
    }

    @Test
    void addLinksForSaleLineAddsSelfLink() {
        // Arrange
        SaleLineResponseDTO dto = new SaleLineResponseDTO(
                "SL-1234ABCD", "ana@aeiou.com", "DS-1A2B3C4DE", 14.99, "EUR");

        // Act
        _linkProvider.addLinksForSaleLine(dto, "pedro@aeiou.com", "SA-1234ABCD", "SL-1234ABCD");

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getRequiredLink("self").getHref()
                .endsWith("/sales/SA-1234ABCD/sale-lines/SL-1234ABCD"));
    }

    @Test
    void addLinksForSaleLineAddsSaleLink() {
        // Arrange
        SaleLineResponseDTO dto = new SaleLineResponseDTO(
                "SL-1234ABCD", "ana@aeiou.com", "DS-1A2B3C4DE", 14.99, "EUR");

        // Act
        _linkProvider.addLinksForSaleLine(dto, "pedro@aeiou.com", "SA-1234ABCD", "SL-1234ABCD");

        // Assert
        assertTrue(dto.hasLink("sale"));
        assertTrue(dto.getRequiredLink("sale").getHref().endsWith("/sales/SA-1234ABCD"));
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