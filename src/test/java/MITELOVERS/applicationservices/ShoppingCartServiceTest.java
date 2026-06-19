package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.repository.IShoppingCartRepo;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.shoppingcart.ShoppingCartLineFactory;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private IShoppingCartRepo _shoppingCartrepo;

    @Mock
    private DirectSaleService _directSaleService;

    @Mock
    private ShoppingCartLineFactory _shoppingCartLineFactory;

    @InjectMocks
    private ShoppingCartService _service;

    @Test
    void findCartByCartIdReturnsCartWhenFound() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));

        // Act
        ShoppingCart result = _service.findCartByCartId("SC-A49F78E2");

        // Assert
        assertSame(cartDouble, result);
    }

    @Test
    void findCartByCartIdThrowsWhenNotFound() {
        // Arrange
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartByCartId("SC-A49F78E2"));
    }

    @Test
    void findCartByUserIdReturnsCartWhenFound() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(_shoppingCartrepo.findShoppingCartByUserId(any())).thenReturn(Optional.of(cartDouble));

        // Act
        ShoppingCart result = _service.findCartByUserId("pedro@aeiou.com");

        // Assert
        assertSame(cartDouble, result);
    }

    @Test
    void findCartByUserIdThrowsWhenNotFound() {
        // Arrange
        when(_shoppingCartrepo.findShoppingCartByUserId(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartByUserId("pedro@aeiou.com"));
    }

    @Test
    void clearShoppingCartLinesReturnsClearedCart() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));
        when(_shoppingCartrepo.save(cartDouble)).thenReturn(cartDouble);

        // Act
        ShoppingCart result = _service.clearShoppingCartLines("SC-A49F78E2");

        // Assert
        assertSame(cartDouble, result);
    }

    @Test
    void clearShoppingCartLinesThrowsWhenCartNotFound() {
        // Arrange
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.clearShoppingCartLines("SC-A49F78E2"));
    }

    @Test
    void findCartLineByLineCartIdReturnsLineWhenFound() {
        // Arrange
        ShoppingCartLineId shoppingCartLineId = new ShoppingCartLineId("SCL-1234ABCD");

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        when(lineDouble.identity()).thenReturn(shoppingCartLineId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));

        // Act
        ShoppingCartLine result = _service.findCartLineByLineCartId("SC-A49F78E2", "SCL-1234ABCD");

        // Assert
        assertSame(lineDouble, result);
    }

    @Test
    void findCartLineByLineCartIdThrowsWhenLineNotFound() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of());
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartLineByLineCartId("SC-A49F78E2", "SCL-1234ABCD"));
    }

    @Test
    void findCartLineByLineCartIdThrowsWhenCartNotFound() {
        // Arrange
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartLineByLineCartId("SC-A49F78E2", "SCL-1234ABCD"));
    }

    @Test
    void addCartLineToCartReturnsUpdatedCart() {
        // Arrange
        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        UserId sellerIdDouble = mock(UserId.class);
        Price priceDouble = mock(Price.class);

        DirectSale directSaleDouble = mock(DirectSale.class);
        when(directSaleDouble.identity()).thenReturn(directSaleIdDouble);
        when(directSaleDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(directSaleDouble.getPrice()).thenReturn(priceDouble);

        ShoppingCartLine newLineDouble = mock(ShoppingCartLine.class);
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        ShoppingCart savedCartDouble = mock(ShoppingCart.class);

        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));
        when(_directSaleService.getDirectSaleById("DS-1A2B3C4DE")).thenReturn(directSaleDouble);
        when(_shoppingCartLineFactory.createNewShoppingCartLine(
                directSaleIdDouble, sellerIdDouble, priceDouble)).thenReturn(newLineDouble);
        when(_shoppingCartrepo.save(cartDouble)).thenReturn(savedCartDouble);

        // Act
        ShoppingCart result = _service.addCartLineToCart("SC-A49F78E2", "DS-1A2B3C4DE");

        // Assert
        assertSame(savedCartDouble, result);
    }

    @Test
    void addCartLineToCartThrowsWhenCartNotFound() {
        // Arrange
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.addCartLineToCart("SC-A49F78E2", "DS-1A2B3C4DE"));
    }

    @Test
    void addCartLineToCartThrowsWhenDirectSaleNotFound() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));
        when(_directSaleService.getDirectSaleById("DS-1A2B3C4DE"))
                .thenThrow(new NoSuchElementException("DirectSale not found"));

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.addCartLineToCart("SC-A49F78E2", "DS-1A2B3C4DE"));
    }

    @Test
    void deleteCartLineByLineCartIdReturnsUpdatedCart() {
        // Arrange
        ShoppingCartLineId lineId = new ShoppingCartLineId("SCL-1234ABCD");

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        when(lineDouble.identity()).thenReturn(lineId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));

        ShoppingCart savedCartDouble = mock(ShoppingCart.class);

        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));
        when(_shoppingCartrepo.save(cartDouble)).thenReturn(savedCartDouble);

        // Act
        ShoppingCart result = _service.deleteCartLineByLineCartId("SC-A49F78E2", "SCL-1234ABCD");

        // Assert
        assertSame(savedCartDouble, result);
    }

    @Test
    void deleteCartLineByLineCartIdThrowsWhenCartNotFound() {
        // Arrange
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.deleteCartLineByLineCartId("SC-A49F78E2", "SCL-1234ABCD"));
    }

    @Test
    void deleteCartLineByLineCartIdThrowsWhenLineNotFound() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of());
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.deleteCartLineByLineCartId("SC-A49F78E2", "SCL-1234ABCD"));
    }
}