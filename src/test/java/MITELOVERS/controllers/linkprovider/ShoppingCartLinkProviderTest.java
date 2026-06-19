package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        when(cartDouble.getCartLines()).thenReturn(List.of(otherLineDouble)); // lineDouble not present

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
        verifyNoInteractions(_authorizationPolicy);
        verify(cartDouble, never()).getCartLines();
    }
}