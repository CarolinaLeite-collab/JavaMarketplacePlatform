package MITELOVERS.domain.shoppingcart;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartTest {

    @Mock
    private ShoppingCartId _cartIdDouble;

    @Mock
    private UserId _buyerIdDouble;

    @Mock
    private Price _totalAmountDouble;

    private List<ShoppingCartLine> _cartLinesDouble;

    @BeforeEach
    void setUp() {

        _cartLinesDouble = new ArrayList<>();
        _cartLinesDouble.add(mock(ShoppingCartLine.class));

    }

    @Test
    void testConstructorWithOneArgument() {

        //SUT
        new ShoppingCart(_buyerIdDouble);

    }

    @Test
    void testConstructorWithFourArguments() {

        //SUT
        new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

    }

    @Test
    void testShouldThrowIfCartItemsIsEmptyAndTotalAmountIsNotNull() {

        List<ShoppingCartLine> cartLinesDouble = new ArrayList<>();

        //SUT + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, cartLinesDouble)
        );

    }

    @Test
    void testShouldThrowIfCartItemsIsNotEmptyAndTotalAmountIsNull() {

        //SUT + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingCart(_cartIdDouble, _buyerIdDouble, null, _cartLinesDouble)
        );

    }

    @Test
    void testIdentity() {

        //Arrange + SUT
        ShoppingCart shoppingCart = new ShoppingCart(_buyerIdDouble);

        //Act
        ShoppingCartId result = shoppingCart.identity();

        //Assert
        assertNotNull(result);

    }

    @Test
    void testSameAsEqualObjectsShouldReturnTrue() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Assert
        assertTrue(cart1.sameAs(cart2));

    }

    @Test
    void testSameAsNonEqualObjectsShouldReturnFalse() {

        //Arrange
        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Assert
        assertFalse(cart1.sameAs(cart2));

    }

    @Test
    void testEqualsNullShouldReturnFalse() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Assert
        assertFalse(cart1.equals(null));

    }

    @Test
    void testEqualsOtherClassShouldReturnFalse() {

        //Arrange
        String string = "test";

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Assert
        assertFalse(cart1.equals(string));

    }

    @Test
    void testEqualsShouldReturnFalseWhenCartsAreDifferent() {

        //Arrange
        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Assert
        assertFalse(cart1.equals(cart2));

    }

    @Test
    void testEqualsShouldReturnTrueWhenCartsAreEquals() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Assert
        assertTrue(cart1.equals(cart2));

    }

    @Test
    void testEqualsShouldReturnTrueWhenCartsIdAreSame() {

        //Arrange
        Price totalAmount2Double = mock(Price.class);


        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmount2Double, _cartLinesDouble);

        //Assert
        assertTrue(cart1.equals(cart2));

    }

    @Test
    void testEqualsShouldReturnTrueWhenCartsIsSame() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Assert
        assertTrue(cart1.equals(cart1));

    }

    @Test
    void testHashShouldBeDifferentWhenCartsAreDifferent() {

        //Arrange
        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Assert
        assertNotEquals(cart1.hashCode(), cart2.hashCode());

    }

    @Test
    void testHashShouldBeSameWhenCartsAreSame() {

        //Arrange
        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Assert
        assertEquals(cart1.hashCode(), cart2.hashCode());

    }

    @Test
    void testGetBuyerIdShouldReturnUserId() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Act
        UserId result = cart1.getBuyerId();

        //Assert
        assertEquals(_buyerIdDouble, result);

    }

    @Test
    void testGetTotalAmountShouldGetTotalAmount() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Act
        Price result = cart1.getTotalAmount();

        //Assert
        assertEquals(_totalAmountDouble, result);

    }

    @Test
    void testGetCartItemsShouldReturnShoppingCartLines() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Act
        List<ShoppingCartLine> result = cart1.getCartLines();

        //Assert
        assertEquals(_cartLinesDouble, result);

    }

    @Test
    void testClearShoppingCartShouldClearCartItemsAndResetTotalAmount() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Act
        cart1.clearShoppingCart();

        //Assert
        assertEquals(0, cart1.getCartLines().size());
        assertNull(cart1.getTotalAmount());

    }


    @Test
    void testAddCartLineShouldAddACartLine() {

        //Arrange
        ShoppingCartLine cartLineDouble = mock(ShoppingCartLine.class);
        Price priceAtAddition = mock(Price.class);
        when(priceAtAddition.getValue()).thenReturn(20.0);
        when(priceAtAddition.getCurrency()).thenReturn(Currency.EUR);
        when(cartLineDouble.getPriceAtAddition()).thenReturn(priceAtAddition);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Act
        cart1.clearShoppingCart();
        cart1.addCartLine(cartLineDouble);

        //Assert
        assertEquals(1, cart1.getCartLines().size());

    }

    @Test
    void testAddCartLineShouldThrowWhenCurrencyIsDifferent() {

        // Arrange
        ShoppingCartLine existingLineDouble = mock(ShoppingCartLine.class);
        Price existingPriceDouble = mock(Price.class);
        when(existingPriceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(existingPriceDouble.getValue()).thenReturn(20.0);
        when(existingLineDouble.getPriceAtAddition()).thenReturn(existingPriceDouble);

        ShoppingCartLine newLineDouble = mock(ShoppingCartLine.class);
        Price newPriceDouble = mock(Price.class);
        when(newPriceDouble.getCurrency()).thenReturn(Currency.USD);
        when(newLineDouble.getPriceAtAddition()).thenReturn(newPriceDouble);

        //SUT
        ShoppingCart cart = new ShoppingCart(_buyerIdDouble);
        cart.addCartLine(existingLineDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addCartLine(newLineDouble));
    }

    @Test
    void testAddCartLineShouldThrowWhenCartLineIsNull() {

        //SUT
        ShoppingCart cart = new ShoppingCart(_buyerIdDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addCartLine(null));
    }

    @Test
    void testRemoveCartLineShouldRemoveACartLine() {

        //Arrange
        ShoppingCartLine cartLineDouble = mock(ShoppingCartLine.class);
        ShoppingCartLineId cartLineDoubleId = mock(ShoppingCartLineId.class);
        Price priceAtAddition = mock(Price.class);
        when(priceAtAddition.getValue()).thenReturn(20.0);
        when(priceAtAddition.getCurrency()).thenReturn(Currency.EUR);
        when(cartLineDouble.getPriceAtAddition()).thenReturn(priceAtAddition);
        when(cartLineDouble.identity()).thenReturn(cartLineDoubleId);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartLinesDouble);

        //Act
        cart1.clearShoppingCart();
        cart1.addCartLine(cartLineDouble);
        cart1.removeCartLine(cartLineDoubleId);

        //Assert
        assertTrue(cart1.getCartLines().isEmpty());

    }

    @Test
    void testRemoveCartLineShouldOnlyRemoveTheCorrectLine() {

        // Arrange
        ShoppingCartLineId lineId1Double = mock(ShoppingCartLineId.class);
        ShoppingCartLineId lineId2Double = mock(ShoppingCartLineId.class);

        ShoppingCartLine line1Double = mock(ShoppingCartLine.class);
        ShoppingCartLine line2Double = mock(ShoppingCartLine.class);

        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);

        when(line1Double.getPriceAtAddition()).thenReturn(priceDouble);
        when(line2Double.getPriceAtAddition()).thenReturn(priceDouble);
        when(line1Double.identity()).thenReturn(lineId1Double);
        when(line2Double.identity()).thenReturn(lineId2Double);

        //SUT
        ShoppingCart cart = new ShoppingCart(_buyerIdDouble);

        // Act
        cart.addCartLine(line1Double);
        cart.addCartLine(line2Double);
        cart.removeCartLine(lineId1Double);

        // Assert
        assertEquals(1, cart.getCartLines().size());
        assertFalse(cart.getCartLines().contains(line1Double));
        assertTrue(cart.getCartLines().contains(line2Double));
    }

    @Test
    void testRemoveCartLineShouldThrowWhenLineNotFound() {

        // SUT + Arrange
        ShoppingCart cart1 = new ShoppingCart(_buyerIdDouble);
        ShoppingCartLineId nonExistentIdDouble = mock(ShoppingCartLineId.class);

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> cart1.removeCartLine(nonExistentIdDouble));
    }

    @Test
    void testRemoveCartLineShouldRecalculateTotalAmount() {

        // Arrange
        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        when(lineDouble.identity()).thenReturn(lineIdDouble);

        //SUT
        ShoppingCart cart = new ShoppingCart(_buyerIdDouble);

        // Act
        cart.addCartLine(lineDouble);
        cart.removeCartLine(lineIdDouble);

        // Assert
        assertNull(cart.getTotalAmount());
    }

    @Test
    void testAddCartLineShouldRecalculateTotalAmount() {

        // Arrange
        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);

        //SUT
        ShoppingCart cart = new ShoppingCart(_buyerIdDouble);

        // Act
        cart.addCartLine(lineDouble);

        // Assert
        assertEquals(lineDouble.getPriceAtAddition().getValue(), 10.0);
    }

}