package MITELOVERS.domain.shoppingcart;

import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.ShoppingCartLineId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ShoppingCartLineTest {

    @Mock
    private ShoppingCartLineId _shoppingCartLineIdDouble;

    @Mock
    private DirectSaleId _directSaleIdDouble;

    @Mock
    private UserId _sellerIdDouble;

    @Mock
    private Price _priceAtAdditionDouble;

    private LocalDateTime _localDateTime;

    @BeforeEach
    void setUp() {

        _localDateTime = LocalDateTime.now();

    }

    @Test
    void testFourArgumentConstructor() {

        //SUT
        new ShoppingCartLine(_directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble);

    }

    @Test
    void testSixArgumentConstructor() {

        //SUT
        new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

    }

    @Test
    void testConstructorShouldThrowIfDirectSaleIdIsNull() {

        //SUT + Assert
        assertThrows(NullPointerException.class,
                () -> new ShoppingCartLine(null, _sellerIdDouble, _priceAtAdditionDouble)
        );

    }

    @Test
    void testConstructorShouldThrowIfUserIdIsNull() {

        //SUT + Assert
        assertThrows(NullPointerException.class,
                () -> new ShoppingCartLine(_directSaleIdDouble, null, _priceAtAdditionDouble)
        );

    }

    @Test
    void testConstructorShouldThrowIfPriceAtAdditionIsNull() {

        //SUT + Assert
        assertThrows(NullPointerException.class,
                () -> new ShoppingCartLine(_directSaleIdDouble, _sellerIdDouble, null)
        );

    }

    @Test
    void testConstructorShouldThrowIfShoppingCartIdIsNull() {

        //SUT + Assert
        assertThrows(NullPointerException.class,
                () -> new ShoppingCartLine(null, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime)

        );

    }

    @Test
    void testConstructorShouldThrowIfTimeOfAdditionIsNull() {

        //SUT + Assert
        assertThrows(NullPointerException.class,
                () -> new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, null)

        );

    }

    @Test
    void testGetDirectSaleId() {

        //SUT
        ShoppingCartLine cartLine = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        DirectSaleId result = cartLine.getDirectSaleId();

        //Assert
        assertEquals(result, _directSaleIdDouble);

    }



    @Test
    void testGetUserId() {

        //SUT
        ShoppingCartLine cartLine = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        UserId result = cartLine.getSellerId();

        //Assert
        assertEquals(result, _sellerIdDouble);

    }

    @Test
    void testGetPriceAtAddition() {

        //SUT
        ShoppingCartLine cartLine = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        Price result = cartLine.getPriceAtAddition();

        //Assert
        assertEquals(result, _priceAtAdditionDouble);

    }

    @Test
    void testGetAddedAt() {

        //SUT
        ShoppingCartLine cartLine = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        LocalDateTime result = cartLine.getAddedAt();

        //Assert
        assertEquals(result, _localDateTime);

    }

    @Test
    void testIdentity() {

        //SUT
        ShoppingCartLine cartLine = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        ShoppingCartLineId result = cartLine.identity();

        //Assert
        assertEquals(result, _shoppingCartLineIdDouble);

    }

    @Test
    void testSameAsShouldReturnTrueWhenObjectIsTheSame() {

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);
        ShoppingCartLine cartLine2 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.sameAs(cartLine2);

        //Assert
        assertTrue(result);

    }

    @Test
    void testSameAsShouldReturnFalseWhenObjectIsNotTheSame() {

        //Arrange
        DirectSaleId directSaleId2Double = mock(DirectSaleId.class);

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);
        ShoppingCartLine cartLine2 = new ShoppingCartLine(_shoppingCartLineIdDouble, directSaleId2Double, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.sameAs(cartLine2);

        //Assert
        assertFalse(result);

    }

    @Test
    void testSameAsShouldReturnFalseWhenComparedToDifferentClass() {

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.sameAs("not a cart line");

        //Assert
        assertFalse(result);

    }

    @Test
    void testSameAsShouldReturnFalseWhenComparedToNull() {

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.sameAs(null);

        //Act + Assert
        assertFalse(result);

    }

    @Test
    void testEqualsShouldReturnTrueWhenIdsAreSameButAttributesDiffer() {

        //Arrange
        DirectSaleId directSaleId2Double = mock(DirectSaleId.class);

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);
        ShoppingCartLine cartLine2 = new ShoppingCartLine(_shoppingCartLineIdDouble, directSaleId2Double, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.equals(cartLine2);

        //Assert
        assertTrue(result);

    }

    @Test
    void testEqualsShouldReturnFalseWhenObjectIsNull() {

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.equals(null);

        //Assert
        assertFalse(result);

    }

    @Test
    void testEqualsShouldReturnFalseWhenObjectIsOfADifferentClass() {

        //Arrange
        String string = "test";

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.equals(string);

        //Assert
        assertFalse(result);

    }

    @Test
    void testEqualsShouldReturnTrueWhenObjectIsTheSameObject() {

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.equals(cartLine1);

        //Act + Assert
        assertTrue(result);

    }

    @Test
    void testEqualsShouldReturnTrueWhenObjectIsEquals() {

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);
        ShoppingCartLine cartLine2 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.equals(cartLine2);

        //Assert
        assertTrue(result);

    }

    @Test
    void testEqualsShouldReturnFalseWhenObjectIsNotTheSame() {

        //Arrange
        ShoppingCartLineId lineId2Double = mock(ShoppingCartLineId.class);

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);
        ShoppingCartLine cartLine2 = new ShoppingCartLine(lineId2Double, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act
        boolean result = cartLine1.equals(cartLine2);

        //Assert
        assertFalse(result);

    }

    @Test
    void testHashCodeShouldBeSameWhenObjectsAreEquals() {

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);
        ShoppingCartLine cartLine2 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act + Assert
        assertEquals(cartLine1.hashCode(), cartLine2.hashCode());

    }

    @Test
    void testHashCodeShouldBeDifferentWhenObjectsAreNotEquals() {

        //Arrange
        ShoppingCartLineId lineId2Double = mock(ShoppingCartLineId.class);

        //SUT
        ShoppingCartLine cartLine1 = new ShoppingCartLine(_shoppingCartLineIdDouble, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);
        ShoppingCartLine cartLine2 = new ShoppingCartLine(lineId2Double, _directSaleIdDouble, _sellerIdDouble, _priceAtAdditionDouble, _localDateTime);

        //Act + Assert
        assertNotEquals(cartLine1.hashCode(), cartLine2.hashCode());

    }





}