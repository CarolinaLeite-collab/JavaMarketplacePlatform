package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.ShoppingCartLineId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.ShoppingCartLineResponseDTO;
import MITELOVERS.dto.response.ShoppingCartResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    void getAllowedMethodsForCartReturnsOnlyOptionsWhenNotAuthorized() {
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
    }

    @Test
    void getAllowedMethodsForCartLinesReturnsGetAndPostWhenOwnerAndAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canGetShoppingCartLines(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canPostShoppingCartLines(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForCartLines(userDouble, cartDouble);

        // Assert
        assertEquals(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsForCartLinesReturnsOnlyOptionsWhenNotAuthorized() {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        when(_authorizationPolicy.canGetShoppingCartLines(userDouble)).thenReturn(false);
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
    void getAllowedMethodsForCartLineReturnsOnlyOptionsWhenNotAuthorized() {
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
    }

    @Test
    void addLinksForUserCartAddsSelfLinkWhenCartIsEmpty() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of());

        ShoppingCartResponseDTO dto = new ShoppingCartResponseDTO("SC-A49F78E2", "pedro@aeiou.com", null, null);

        // Act
        _linkProvider.addLinksForUserCart(dto, "pedro@aeiou.com", "SC-A49F78E2", cartDouble);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.getRequiredLink("self").getHref().endsWith("/shopping-carts/SC-A49F78E2"));
        assertFalse(dto.hasLink("shopping-cart-line"));
    }

    @Test
    void addLinksForUserCartAddsLineLinksWhenCartHasLines() {
        // Arrange
        ShoppingCartLineId lineId = new ShoppingCartLineId("SCL-1234ABCD");

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        when(lineDouble.identity()).thenReturn(lineId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));

        ShoppingCartResponseDTO dto = new ShoppingCartResponseDTO("SC-A49F78E2", "pedro@aeiou.com", null, null);

        // Act
        _linkProvider.addLinksForUserCart(dto, "pedro@aeiou.com", "SC-A49F78E2", cartDouble);

        // Assert
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.hasLink("shopping-cart-line"));
        assertTrue(dto.getRequiredLink("shopping-cart-line").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD"));
    }

    @Test
    void addLinksForUserCartLineAddsSelfLink() {
        // Arrange
        ShoppingCartLineResponseDTO dto = new ShoppingCartLineResponseDTO(
                "SCL-1234ABCD", "DS-1A2B3C4DE", "ana@aeiou.com", 14.99, "EUR", "2026-06-18T15:30:00");

        // Act
        _linkProvider.addLinksForUserCartLine(dto, "pedro@aeiou.com", "SC-A49F78E2", "SCL-1234ABCD", "DS-1A2B3C4DE");

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

        // Act
        _linkProvider.addLinksForUserCartLine(dto, "pedro@aeiou.com", "SC-A49F78E2", "SCL-1234ABCD", "DS-1A2B3C4DE");

        // Assert
        assertTrue(dto.hasLink("direct-sale"));
        assertTrue(dto.getRequiredLink("direct-sale").getHref()
                .endsWith("/direct-sales/DS-1A2B3C4DE"));
    }

    @Test
    void addLinksForDeleteUserCartLineAddsShoppingCartLink() {
        // Arrange
        RepresentationModel<?> model = new RepresentationModel<>();

        // Act
        _linkProvider.addLinksForDeleteUserCartLine(model, "pedro@aeiou.com", "SC-A49F78E2");

        // Assert
        assertTrue(model.hasLink("shopping-cart"));
        assertTrue(model.getRequiredLink("shopping-cart").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2"));
    }

    @Test
    void addLinksForCreateUserCartLineAddsSelfLink() {
        // Arrange
        RepresentationModel<?> model = new RepresentationModel<>();

        // Act
        _linkProvider.addLinksForCreateUserCartLine(model, "pedro@aeiou.com", "SC-A49F78E2", "SCL-1234ABCD");

        // Assert
        assertTrue(model.hasLink("self"));
        assertTrue(model.getRequiredLink("self").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD"));
    }

    @Test
    void addLinksForCreateUserCartLineAddsShoppingCartLink() {
        // Arrange
        RepresentationModel<?> model = new RepresentationModel<>();

        // Act
        _linkProvider.addLinksForCreateUserCartLine(model, "pedro@aeiou.com", "SC-A49F78E2", "SCL-1234ABCD");

        // Assert
        assertTrue(model.hasLink("shopping-cart"));
        assertTrue(model.getRequiredLink("shopping-cart").getHref()
                .endsWith("/shopping-carts/SC-A49F78E2"));
    }
}