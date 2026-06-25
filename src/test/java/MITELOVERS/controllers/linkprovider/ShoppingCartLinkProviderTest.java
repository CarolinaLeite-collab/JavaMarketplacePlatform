package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.ShoppingCartLineResponseDTO;
import MITELOVERS.dto.response.ShoppingCartResponseDTO;
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
class ShoppingCartLinkProviderTest {

    @Mock
    private AuthorizationPolicy _authorizationPolicy;

    @InjectMocks
    private ShoppingCartLinkProvider _linkProvider;

    @Test
    void getAllowedMethodsForCartsReturnsGetAndOptionsWhenAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canGetShoppingCart(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCarts(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.GET, HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForCartsReturnsOnlyOptionsWhenNotAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canGetShoppingCart(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCarts(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForCartReturnsGetAndPatchWhenOwnerAndAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canGetShoppingCart(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canPatchShoppingCart(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCart(userDouble, cartDouble);

        // Assert
        assertEquals(List.of(HttpMethod.GET, HttpMethod.PATCH, HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForCartReturnsOnlyOptionsWhenOwnerButNotAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canGetShoppingCart(userDouble)).thenReturn(false);
        when(_authorizationPolicy.canPatchShoppingCart(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCart(userDouble, cartDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForCartReturnsOnlyOptionsWhenNotOwner() {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCart(userDouble, cartDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
        verifyNoInteractions(_authorizationPolicy);
    }

    @Test
    void getAllowedMethodsForCartLinesReturnsPostWhenOwnerAndAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canPostShoppingCartLines(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCartLines(userDouble, cartDouble);

        // Assert
        assertEquals(List.of(HttpMethod.POST, HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForCartLinesReturnsOnlyOptionsWhenOwnerButNotAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canPostShoppingCartLines(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCartLines(userDouble, cartDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForCartLinesReturnsOnlyOptionsWhenNotOwner() {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCartLines(userDouble, cartDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
        verifyNoInteractions(_authorizationPolicy);
    }

    @Test
    void getAllowedMethodsForCartLineReturnsGetAndDeleteWhenOwnerLineExistsAndAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));

        when(_authorizationPolicy.canGetShoppingCartLine(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canDeleteShoppingCartLine(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCartLine(userDouble, cartDouble, lineDouble);

        // Assert
        assertEquals(List.of(HttpMethod.GET, HttpMethod.DELETE, HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForCartLineReturnsOnlyOptionsWhenOwnerButNotAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));

        when(_authorizationPolicy.canGetShoppingCartLine(userDouble)).thenReturn(false);
        when(_authorizationPolicy.canDeleteShoppingCartLine(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCartLine(userDouble, cartDouble, lineDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForCartLineReturnsOnlyOptionsWhenOwnerButLineNotInCart() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        ShoppingCartLine otherLineDouble = mock(ShoppingCartLine.class);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);
        when(cartDouble.getCartLines()).thenReturn(List.of(otherLineDouble));

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCartLine(userDouble, cartDouble, lineDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
        verifyNoInteractions(_authorizationPolicy);
    }

    @Test
    void getAllowedMethodsForCartLineReturnsOnlyOptionsWhenNotOwner() {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCartLine(userDouble, cartDouble, lineDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
        verifyNoInteractions(_authorizationPolicy);
    }

    @Test
    void getLinksReturnsShoppingCartLinkWhenAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canGetShoppingCart(userDouble)).thenReturn(true);

        // Act
        List<Link> result = _linkProvider.getLinks(userDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("shopping-cart", result.get(0).getRel().value());
        assertTrue(result.get(0).getHref().endsWith("/shopping-carts"));
    }

    @Test
    void getLinksReturnsEmptyWhenNotAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canGetShoppingCart(userDouble)).thenReturn(false);

        // Act
        List<Link> result = _linkProvider.getLinks(userDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addLinksForUserCartDiscoveryAddsSelfLink() {
        // Arrange
        RepresentationModel<?> model = new RepresentationModel<>();

        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A49F78E2");

        // Act
        _linkProvider.addLinksForUserCartDiscovery(model, userIdDouble, cartIdDouble);

        // Assert
        assertTrue(model.hasLink("self"));
        assertTrue(model.getRequiredLink("self").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2"));
    }

    @Test
    void addLinksForUserCartAddsSelfLinkWhenCartIsEmpty() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of());

        ShoppingCartResponseDTO dto = new ShoppingCartResponseDTO("SC-A49F78E2", "pedro@aeiou.com", null, null);

        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A49F78E2");

        // Act
        _linkProvider.addLinksForUserCart(dto, userIdDouble, cartIdDouble, cartDouble);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getRequiredLink("self").getHref().endsWith("/shopping-carts/SC-A49F78E2"));
        assertFalse(dto.hasLink("shopping-cart-line"));
        assertFalse(dto.hasLink("sale"));
    }

    @Test
    void addLinksForUserCartAddsLineLinksAndSaleLinkWhenCartHasLines() {
        // Arrange
        ShoppingCartLineId lineId = new ShoppingCartLineId("SCL-1234ABCD");

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        when(lineDouble.identity()).thenReturn(lineId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));

        ShoppingCartResponseDTO dto = new ShoppingCartResponseDTO("SC-A49F78E2", "pedro@aeiou.com", null, null);

        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A49F78E2");

        // Act
        _linkProvider.addLinksForUserCart(dto, userIdDouble, cartIdDouble, cartDouble);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getRequiredLink("self").getHref().endsWith("/shopping-carts/SC-A49F78E2"));
        assertTrue(dto.hasLink("sale"));
        assertTrue(dto.getRequiredLink("sale").getHref().endsWith("/sales"));
        assertTrue(dto.hasLink("shopping-cart-line"));
        assertTrue(dto.getRequiredLink("shopping-cart-line").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD"));
    }

    @Test
    void addLinksForUserCartLineAddsSelfLink() {
        // Arrange
        ShoppingCartLineResponseDTO dto = new ShoppingCartLineResponseDTO(
                "SCL-1234ABCD", "DS-1A2B3C4DE", "ana@aeiou.com", 14.99, "EUR", "2026-06-18T15:30:00");

        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A49F78E2");

        ShoppingCartLineId cartLineIdDouble = mock(ShoppingCartLineId.class);
        when(cartLineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        // Act
        _linkProvider.addLinksForUserCartLine(dto, userIdDouble, cartIdDouble, cartLineIdDouble, directSaleIdDouble);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getRequiredLink("self").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD"));
    }

    @Test
    void addLinksForUserCartLineAddsDirectSaleLink() {
        // Arrange
        ShoppingCartLineResponseDTO dto = new ShoppingCartLineResponseDTO(
                "SCL-1234ABCD", "DS-1A2B3C4DE", "ana@aeiou.com", 14.99, "EUR", "2026-06-18T15:30:00");

        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A49F78E2");

        ShoppingCartLineId cartLineIdDouble = mock(ShoppingCartLineId.class);
        when(cartLineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        // Act
        _linkProvider.addLinksForUserCartLine(dto, userIdDouble, cartIdDouble, cartLineIdDouble, directSaleIdDouble);

        // Assert
        assertTrue(dto.hasLink("direct-sale"));
        assertTrue(dto.getRequiredLink("direct-sale").getHref()
                .endsWith("/direct-sales/DS-1A2B3C4DE"));
    }

    @Test
    void addLinksForDeleteUserCartLineAddsShoppingCartLink() {
        // Arrange
        RepresentationModel<?> model = new RepresentationModel<>();

        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A49F78E2");

        // Act
        _linkProvider.addLinksForDeleteUserCartLine(model, userIdDouble, cartIdDouble);

        // Assert
        assertTrue(model.hasLink("shopping-cart"));
        assertTrue(model.getRequiredLink("shopping-cart").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2"));
    }

    @Test
    void addLinksForCreateUserCartLineAddsSelfLink() {
        // Arrange
        RepresentationModel<?> model = new RepresentationModel<>();

        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A49F78E2");

        ShoppingCartLineId cartLineIdDouble = mock(ShoppingCartLineId.class);
        when(cartLineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        // Act
        _linkProvider.addLinksForCreateUserCartLine(model, userIdDouble, cartIdDouble, cartLineIdDouble);

        // Assert
        assertTrue(model.hasLink("self"));
        assertTrue(model.getRequiredLink("self").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD"));
    }

    @Test
    void addLinksForCreateUserCartLineAddsShoppingCartLink() {
        // Arrange
        RepresentationModel<?> model = new RepresentationModel<>();

        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A49F78E2");

        ShoppingCartLineId cartLineIdDouble = mock(ShoppingCartLineId.class);
        when(cartLineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        // Act
        _linkProvider.addLinksForCreateUserCartLine(model, userIdDouble, cartIdDouble, cartLineIdDouble);

        // Assert
        assertTrue(model.hasLink("shopping-cart"));
        assertTrue(model.getRequiredLink("shopping-cart").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2"));
    }
}