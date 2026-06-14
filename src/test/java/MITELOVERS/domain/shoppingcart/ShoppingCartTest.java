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

    private List<ShoppingCartLine> _cartLinesDouble;

    @BeforeEach
    void setUp() {
        _cartLinesDouble = new ArrayList<>();
    }

    @Test
    void testConstructorWithOneArgument() {

        //SUT
        new ShoppingCart(_buyerIdDouble);

    }

    @Test
    void testConstructorWithFourArguments() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

    }

    @Test
    void testShouldThrowIfCartItemsIsEmptyAndTotalAmountIsNotNull() {

        //Arrange
        Price totalAmountDouble = mock(Price.class);

        //SUT + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble)
        );

    }

    @Test
    void testShouldThrowIfCartItemsIsNotEmptyAndTotalAmountIsNull() {

        //Arrange
        // Constructor flow: cartLines non-empty + totalAmount null -> throws BEFORE
        // any currency check or recalculateTotalAmount() runs, so getCurrency()/getValue()
        // on this line's Price are never called. Only getPriceAtAddition() itself is
        // not even invoked here, so no stubbing is needed on the line at all.
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        _cartLinesDouble.add(lineDouble);

        //SUT + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingCart(_cartIdDouble, _buyerIdDouble, null, _cartLinesDouble)
        );

    }

    @Test
    void testShouldThrowIfCartLinesHaveDifferentCurrencies() {

        //Arrange
        // Both totalAmount-null/empty checks pass (non-empty cart, non-null totalAmount),
        // so execution reaches allCartLinesShareSameCurrency(). That method only calls
        // getPriceAtAddition().getCurrency() on each line - it never calls getValue(),
        // and it throws on the second line before recalculateTotalAmount() runs.
        ShoppingCartLine line1Double = mock(ShoppingCartLine.class);
        Price price1Double = mock(Price.class);
        when(price1Double.getCurrency()).thenReturn(Currency.EUR);
        when(line1Double.getPriceAtAddition()).thenReturn(price1Double);

        ShoppingCartLine line2Double = mock(ShoppingCartLine.class);
        Price price2Double = mock(Price.class);
        when(price2Double.getCurrency()).thenReturn(Currency.USD);
        when(line2Double.getPriceAtAddition()).thenReturn(price2Double);

        _cartLinesDouble.add(line1Double);
        _cartLinesDouble.add(line2Double);

        Price totalAmountDouble = mock(Price.class);

        //SUT + Assert
        assertThrows(IllegalStateException.class,
                () -> new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble)
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

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Assert
        assertTrue(cart1.sameAs(cart2));

    }

    @Test
    void testSameAsNonEqualObjectsShouldReturnFalse() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);
        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Assert
        assertFalse(cart1.sameAs(cart2));

    }

    @Test
    void testEqualsNullShouldReturnFalse() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Assert
        assertFalse(cart1.equals(null));

    }

    @Test
    void testEqualsOtherClassShouldReturnFalse() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        String string = "test";
        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Assert
        assertFalse(cart1.equals(string));

    }

    @Test
    void testEqualsShouldReturnFalseWhenCartsAreDifferent() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);
        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Assert
        assertFalse(cart1.equals(cart2));

    }

    @Test
    void testEqualsShouldReturnTrueWhenCartsAreEquals() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Assert
        assertTrue(cart1.equals(cart2));

    }

    @Test
    void testEqualsShouldReturnTrueWhenCartsIdAreSame() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmount1Double = mock(Price.class);
        Price totalAmount2Double = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmount1Double, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmount2Double, _cartLinesDouble);

        //Assert
        assertTrue(cart1.equals(cart2));

    }

    @Test
    void testEqualsShouldReturnTrueWhenCartsIsSame() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Assert
        assertTrue(cart1.equals(cart1));

    }

    @Test
    void testHashShouldBeDifferentWhenCartsAreDifferent() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);
        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Assert
        assertNotEquals(cart1.hashCode(), cart2.hashCode());

    }

    @Test
    void testHashShouldBeSameWhenCartsAreSame() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Assert
        assertEquals(cart1.hashCode(), cart2.hashCode());

    }

    @Test
    void testGetBuyerIdShouldReturnUserId() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Act
        UserId result = cart1.getBuyerId();

        //Assert
        assertEquals(_buyerIdDouble, result);

    }

    @Test
    void testGetTotalAmountShouldReturnDerivedAmount() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Act
        Price result = cart1.getTotalAmount();

        //Assert
        assertNotNull(result);
        assertEquals(10.0, result.getValue());
        assertEquals(Currency.EUR, result.getCurrency());

    }

    @Test
    void testGetCartItemsShouldReturnShoppingCartLines() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Act
        List<ShoppingCartLine> result = cart1.getCartLines();

        //Assert
        assertEquals(_cartLinesDouble, result);

    }

    @Test
    void testClearShoppingCartShouldClearCartItemsAndResetTotalAmount() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        _cartLinesDouble.add(lineDouble);

        Price totalAmountDouble = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);

        //Act
        cart1.clearShoppingCart();

        //Assert
        assertEquals(0, cart1.getCartLines().size());
        assertNull(cart1.getTotalAmount());

    }

    @Test
    void testAddCartLineShouldAddACartLine() {

        //Arrange
        // cart starts empty via the 1-arg constructor, so addCartLine triggers
        // isSameCurrencyAsExisting (returns true on empty list, no Price calls)
        // and then recalculateTotalAmount, which calls getCurrency() and getValue().
        ShoppingCartLine cartLineDouble = mock(ShoppingCartLine.class);
        Price priceAtAddition = mock(Price.class);
        when(priceAtAddition.getCurrency()).thenReturn(Currency.EUR);
        when(priceAtAddition.getValue()).thenReturn(20.0);
        when(cartLineDouble.getPriceAtAddition()).thenReturn(priceAtAddition);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_buyerIdDouble);

        //Act
        cart1.addCartLine(cartLineDouble);

        //Assert
        assertEquals(1, cart1.getCartLines().size());

    }

    @Test
    void testAddCartLineShouldThrowWhenCurrencyIsDifferent() {

        // Arrange
        // existingLineDouble is added successfully -> isSameCurrencyAsExisting (empty list,
        // true, no Price calls) then recalculateTotalAmount calls getCurrency() + getValue().
        ShoppingCartLine existingLineDouble = mock(ShoppingCartLine.class);
        Price existingPriceDouble = mock(Price.class);
        when(existingPriceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(existingPriceDouble.getValue()).thenReturn(20.0);
        when(existingLineDouble.getPriceAtAddition()).thenReturn(existingPriceDouble);

        // newLineDouble: isSameCurrencyAsExisting compares its getCurrency() against the
        // existing line's currency and returns false -> throws BEFORE recalculateTotalAmount
        // runs, so getValue() on newPriceDouble is never called.
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
        when(priceAtAddition.getCurrency()).thenReturn(Currency.EUR);
        when(priceAtAddition.getValue()).thenReturn(20.0);
        when(cartLineDouble.getPriceAtAddition()).thenReturn(priceAtAddition);
        when(cartLineDouble.identity()).thenReturn(cartLineDoubleId);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_buyerIdDouble);

        //Act
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
        assertEquals(10.0, cart.getTotalAmount().getValue());
        assertEquals(Currency.EUR, cart.getTotalAmount().getCurrency());
    }

}