package MITELOVERS.domain.shoppingcart;

import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ShoppingCartTest {

    @Mock
    private ShoppingCartId _cartIdDouble;

    @Mock
    private UserId _buyerIdDouble;

    @Mock
    private Price _totalAmountDouble;

    @Mock
    private List<ShoppingCartLine> _cartItemsDouble;

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



}