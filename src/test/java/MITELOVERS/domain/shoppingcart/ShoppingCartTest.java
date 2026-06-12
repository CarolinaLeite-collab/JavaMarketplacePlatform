package MITELOVERS.domain.shoppingcart;

import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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

    private List<ShoppingCartLine> _cartItemsDouble;

    @BeforeEach
    void setUp() {

        _cartItemsDouble = new ArrayList<>();
        _cartItemsDouble.add(mock(ShoppingCartLine.class));

    }

    @Test
    void testConstructorWithOneArgument() {

        //SUT
        new ShoppingCart(_buyerIdDouble);

    }

    @Test
    void testConstructorWithFourArguments() {

        //SUT
        new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

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
                () -> new ShoppingCart(_cartIdDouble, _buyerIdDouble, null, _cartItemsDouble)
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
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Assert
        assertTrue(cart1.sameAs(cart2));

    }

    @Test
    void testSameAsNonEqualObjectsShouldReturnFalse() {

        //Arrange
        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Assert
        assertFalse(cart1.sameAs(cart2));

    }

    @Test
    void testEqualsNullShouldReturnFalse() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Assert
        assertFalse(cart1.equals(null));

    }

    @Test
    void testEqualsOtherClassShouldReturnFalse() {

        //Arrange
        String string = "test";

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Assert
        assertFalse(cart1.equals(string));

    }

    @Test
    void testEqualsShouldReturnFalseWhenCartsAreDifferent() {

        //Arrange
        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Assert
        assertFalse(cart1.equals(cart2));

    }

    @Test
    void testEqualsShouldReturnTrueWhenCartsAreEquals() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Assert
        assertTrue(cart1.equals(cart2));

    }

    @Test
    void testEqualsShouldReturnTrueWhenCartsIdAreSame() {

        //Arrange
        Price totalAmount2Double = mock(Price.class);


        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, totalAmount2Double, _cartItemsDouble);

        //Assert
        assertTrue(cart1.equals(cart2));

    }

    @Test
    void testEqualsShouldReturnTrueWhenCartsIsSame() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Assert
        assertTrue(cart1.equals(cart1));

    }

    @Test
    void testHashShouldBeDifferentWhenCartsAreDifferent() {

        //Arrange
        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);
        ShoppingCart cart2 = new ShoppingCart(cartId2Double, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Assert
        assertNotEquals(cart1.hashCode(), cart2.hashCode());

    }

    @Test
    void testHashShouldBeSameWhenCartsAreSame() {

        //Arrange
        ShoppingCartId cartId2Double = mock(ShoppingCartId.class);

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);
        ShoppingCart cart2 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Assert
        assertEquals(cart1.hashCode(), cart2.hashCode());

    }

    @Test
    void testGetBuyerIdShouldReturnUserId() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Act
        UserId result = cart1.getBuyerId();

        //Assert
        assertEquals(_buyerIdDouble, result);

    }

    @Test
    void testGetTotalAmountShouldGetTotalAmount() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Act
        Price result = cart1.getTotalAmount();

        //Assert
        assertEquals(_totalAmountDouble, result);

    }

    @Test
    void testGetCartItemsShouldReturnShoppingCartLines() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Act
        List<ShoppingCartLine> result = cart1.getCartItems();

        //Assert
        assertEquals(_cartItemsDouble, result);

    }

    @Test
    void testClearShoppingCartShouldClearCartItemsAndResetTotalAmount() {

        //SUT
        ShoppingCart cart1 = new ShoppingCart(_cartIdDouble, _buyerIdDouble, _totalAmountDouble, _cartItemsDouble);

        //Act
        cart1.clearShoppingCart();

        //Assert
        assertEquals(0, cart1.getCartItems().size());
        assertNull(cart1.getTotalAmount());

    }



}