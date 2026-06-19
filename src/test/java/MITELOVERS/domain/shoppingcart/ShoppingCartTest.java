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

        //Act
        boolean result = cart1.sameAs(cart2);

        //Assert
        assertTrue(result);

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
        UserId buyerId2Double = mock(UserId.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmountDouble, _cartLinesDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, buyerId2Double, totalAmountDouble, _cartLinesDouble);

        //Act
        boolean result = cart1.sameAs(cart2);

        //Assert
        assertFalse(result);

    }

    @Test
    void testSameAsShouldReturnFalseWhenCartLinesDiffer() {

        //Arrange
        ShoppingCartLine line1Double = mock(ShoppingCartLine.class);
        Price price1Double = mock(Price.class);
        when(price1Double.getCurrency()).thenReturn(Currency.EUR);
        when(price1Double.getValue()).thenReturn(10.0);
        when(line1Double.getPriceAtAddition()).thenReturn(price1Double);

        ShoppingCartLine line2Double = mock(ShoppingCartLine.class);
        Price price2Double = mock(Price.class);
        when(price2Double.getCurrency()).thenReturn(Currency.EUR);
        when(price2Double.getValue()).thenReturn(99.0);
        when(line2Double.getPriceAtAddition()).thenReturn(price2Double);

        List<ShoppingCartLine> cartLines1 = new ArrayList<>(List.of(line1Double));
        List<ShoppingCartLine> cartLines2 = new ArrayList<>(List.of(line2Double));

        Price totalAmount1Double = mock(Price.class);
        Price totalAmount2Double = mock(Price.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmount1Double, cartLines1);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmount2Double, cartLines2);

        //Act
        boolean result = cart1.sameAs(cart2);

        //Assert
        assertFalse(result);

    }

    @Test
    void testSameAsShouldReturnTrueWhenBothCartsAreEmpty() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_buyerIdDouble);
        ShoppingCart cart2 = new ShoppingCart(_buyerIdDouble);

        //Act
        boolean result = cart1.sameAs(cart2);

        //Assert
        assertTrue(result);

    }

    @Test
    void testSameAsShouldReturnFalseWhenComparedToDifferentClass() {

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
        boolean result = cart1.sameAs("not a cart");

        //Assert
        assertFalse(result);

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

        //Act
        boolean result = cart1.equals(null);

        //Assert
        assertFalse(result);

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

        //Act
        boolean result = cart1.equals(string);

        //Assert
        assertFalse(result);

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

        //Act
        boolean result = cart1.equals(cart2);

        //Assert
        assertFalse(result);

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

        //Act
        boolean result = cart1.equals(cart2);

        //Assert
        assertTrue(result);

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

        //Act
        boolean result = cart1.equals(cart2);

        //Assert
        assertTrue(result);

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

        //Act
        boolean result = cart1.equals(cart1);

        //Assert
        assertTrue(result);

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

        //Act + Assert
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

        //Act + Assert
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

        //Arrange
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

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addCartLine(newLineDouble));
    }

    @Test
    void testAddCartLineShouldThrowWhenDirectSaleIsAlreadyInCart() {

        // Arrange
        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);

        ShoppingCartLine existingLineDouble = mock(ShoppingCartLine.class);
        Price existingPriceDouble = mock(Price.class);
        when(existingPriceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(existingPriceDouble.getValue()).thenReturn(10.0);
        when(existingLineDouble.getPriceAtAddition()).thenReturn(existingPriceDouble);
        when(existingLineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);

        ShoppingCartLine duplicateLineDouble = mock(ShoppingCartLine.class);
        Price duplicatePriceDouble = mock(Price.class);
        when(duplicatePriceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(duplicateLineDouble.getPriceAtAddition()).thenReturn(duplicatePriceDouble);
        when(duplicateLineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);

        ShoppingCart cart = new ShoppingCart(_buyerIdDouble);
        cart.addCartLine(existingLineDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addCartLine(duplicateLineDouble));
    }

    @Test
    void testAddCartLineShouldThrowWhenCartLineIsNull() {

        //SUT
        ShoppingCart cart = new ShoppingCart(_buyerIdDouble);

        //Act + Assert
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

        DirectSaleId directSaleId1Double = mock(DirectSaleId.class);
        DirectSaleId directSaleId2Double = mock(DirectSaleId.class);

        ShoppingCartLine line1Double = mock(ShoppingCartLine.class);
        ShoppingCartLine line2Double = mock(ShoppingCartLine.class);

        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);

        when(line1Double.getPriceAtAddition()).thenReturn(priceDouble);
        when(line2Double.getPriceAtAddition()).thenReturn(priceDouble);
        when(line1Double.identity()).thenReturn(lineId1Double);
        when(line2Double.identity()).thenReturn(lineId2Double);
        when(line1Double.getDirectSaleId()).thenReturn(directSaleId1Double);
        when(line2Double.getDirectSaleId()).thenReturn(directSaleId2Double);

        // SUT
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

        //SUT + Arrange
        ShoppingCart cart1 = new ShoppingCart(_buyerIdDouble);
        ShoppingCartLineId nonExistentIdDouble = mock(ShoppingCartLineId.class);

        //Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> cart1.removeCartLine(nonExistentIdDouble));
    }

    @Test
    void testRemoveCartLineShouldRecalculateTotalAmount() {

        //Arrange
        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        when(lineDouble.identity()).thenReturn(lineIdDouble);

        //SUT
        ShoppingCart cart = new ShoppingCart(_buyerIdDouble);

        //Act
        cart.addCartLine(lineDouble);
        cart.removeCartLine(lineIdDouble);

        //Assert
        assertNull(cart.getTotalAmount());
    }

    @Test
    void testAddCartLineShouldRecalculateTotalAmount() {

        //Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble.getValue()).thenReturn(10.0);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);

        //SUT
        ShoppingCart cart = new ShoppingCart(_buyerIdDouble);

        //Act
        cart.addCartLine(lineDouble);

        //Assert
        assertEquals(10.0, cart.getTotalAmount().getValue());
        assertEquals(Currency.EUR, cart.getTotalAmount().getCurrency());
    }

}