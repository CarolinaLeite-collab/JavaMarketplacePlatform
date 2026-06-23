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
    private IShoppingCartRepo _shoppingCartRepo;

    @Mock
    private DirectSaleService _directSaleService;

    @Mock
    private ShoppingCartLineFactory _shoppingCartLineFactory;

    @InjectMocks
    private ShoppingCartService _service;

    @Test
    void findCartByCartIdReturnsCartWhenFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.of(cartDouble));

        // Act
        ShoppingCart result = _service.findCartByCartId(cartIdDouble);

        // Assert
        assertSame(cartDouble, result);
    }

    @Test
    void findCartByCartIdThrowsWhenNotFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartByCartId(cartIdDouble));
    }

    @Test
    void findCartByUserIdReturnsCartWhenFound() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        when(_shoppingCartRepo.findShoppingCartByUserId(userIdDouble)).thenReturn(Optional.of(cartDouble));

        // Act
        ShoppingCart result = _service.findCartByUserId(userIdDouble);

        // Assert
        assertSame(cartDouble, result);
    }

    @Test
    void findCartByUserIdThrowsWhenNotFound() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);

        when(_shoppingCartRepo.findShoppingCartByUserId(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartByUserId(userIdDouble));
    }

    @Test
    void clearShoppingCartLinesReturnsClearedCart() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.of(cartDouble));
        when(_shoppingCartRepo.save(cartDouble)).thenReturn(cartDouble);

        // Act
        ShoppingCart result = _service.clearShoppingCartLines(cartIdDouble);

        // Assert
        assertSame(cartDouble, result);
    }

    @Test
    void clearShoppingCartLinesThrowsWhenCartNotFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.clearShoppingCartLines(cartIdDouble));
    }

    @Test
    void findCartLineByLineCartIdReturnsLineWhenFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        when(lineDouble.identity()).thenReturn(lineIdDouble);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.of(cartDouble));

        // Act
        ShoppingCartLine result = _service.findCartLineByLineCartId(cartIdDouble, lineIdDouble);

        // Assert
        assertSame(lineDouble, result);
    }

    @Test
    void findCartLineByLineCartIdThrowsWhenLineNotFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of());

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.of(cartDouble));

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartLineByLineCartId(cartIdDouble, lineIdDouble));
    }

    @Test
    void findCartLineByLineCartIdThrowsWhenCartNotFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartLineByLineCartId(cartIdDouble, lineIdDouble));
    }

    @Test
    void addCartLineToCartReturnsNewCartLine() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);

        UserId sellerIdDouble = mock(UserId.class);
        UserId buyerIdDouble = mock(UserId.class);
        Price priceDouble = mock(Price.class);

        DirectSale directSaleDouble = mock(DirectSale.class);
        when(directSaleDouble.identity()).thenReturn(directSaleIdDouble);
        when(directSaleDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(directSaleDouble.getPrice()).thenReturn(priceDouble);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(buyerIdDouble);

        ShoppingCartLine newLineDouble = mock(ShoppingCartLine.class);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.of(cartDouble));
        when(_directSaleService.getDirectSaleById(directSaleIdDouble)).thenReturn(directSaleDouble);
        when(_shoppingCartLineFactory.createNewShoppingCartLine(
                directSaleIdDouble, sellerIdDouble, priceDouble)).thenReturn(newLineDouble);

        // Act
        ShoppingCartLine result = _service.addCartLineToCart(cartIdDouble, directSaleIdDouble);

        // Assert
        assertSame(newLineDouble, result);
    }

    @Test
    void addCartLineToCartThrowsWhenBuyerIsAlsoSeller() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);

        UserId sharedUserId = mock(UserId.class);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        DirectSale directSaleDouble = mock(DirectSale.class);
        when(directSaleDouble.getSellerId()).thenReturn(sharedUserId);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.of(cartDouble));
        when(_directSaleService.getDirectSaleById(directSaleIdDouble)).thenReturn(directSaleDouble);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> _service.addCartLineToCart(cartIdDouble, directSaleIdDouble));
    }

    @Test
    void addCartLineToCartThrowsWhenCartNotFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.addCartLineToCart(cartIdDouble, directSaleIdDouble));
    }

    @Test
    void addCartLineToCartThrowsWhenDirectSaleNotFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.of(cartDouble));
        when(_directSaleService.getDirectSaleById(directSaleIdDouble))
                .thenThrow(new NoSuchElementException("DirectSale not found"));

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.addCartLineToCart(cartIdDouble, directSaleIdDouble));
    }

    @Test
    void deleteCartLineByLineCartIdReturnsUpdatedCart() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        when(lineDouble.identity()).thenReturn(lineIdDouble);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));

        ShoppingCart savedCartDouble = mock(ShoppingCart.class);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.of(cartDouble));
        when(_shoppingCartRepo.save(cartDouble)).thenReturn(savedCartDouble);

        // Act
        ShoppingCart result = _service.deleteCartLineByLineCartId(cartIdDouble, lineIdDouble);

        // Assert
        assertSame(savedCartDouble, result);
    }

    @Test
    void deleteCartLineByLineCartIdThrowsWhenCartNotFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.deleteCartLineByLineCartId(cartIdDouble, lineIdDouble));
    }

    @Test
    void deleteCartLineByLineCartIdThrowsWhenLineNotFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of());

        when(_shoppingCartRepo.ofIdentity(cartIdDouble)).thenReturn(Optional.of(cartDouble));

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.deleteCartLineByLineCartId(cartIdDouble, lineIdDouble));
    }
}