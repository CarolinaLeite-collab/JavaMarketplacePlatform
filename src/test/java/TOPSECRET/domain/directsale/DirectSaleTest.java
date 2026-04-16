package TOPSECRET.domain.directsale;

import TOPSECRET.domain.valueobject.DirectSaleId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DirectSaleTest {

    private List<ItemId> _itemsId;
    private DirectSaleId _dsId;
    private ItemId _itemIdDouble;
    private Price _priceDouble;
    private Period _period;

    @BeforeEach
    void setUp() {

        _itemsId = new ArrayList<>();
        _itemIdDouble = mock(ItemId.class);
        _itemsId.add(_itemIdDouble);
        _priceDouble = mock(Price.class);
        _period = Period.ofMonths(3);
    }

    @Test
    void constructorShouldBuildDirectSaleWithTimeLimit() {

        // Act
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period); // SUT

        // Assert
        assertEquals(_itemsId, directSale.getItemsId());
        assertEquals(_priceDouble, directSale.getPrice());
        assertEquals(_period, directSale.getTimeLimit());
    }

    @Test
    void constructorShouldBuildDirectSaleWithoutTimeLimit() {

        // Act
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, null); // SUT

        // Assert
        assertEquals(_itemsId, directSale.getItemsId());
        assertEquals(_priceDouble, directSale.getPrice());
        assertNull(directSale.getTimeLimit());
    }

    @Test
    void constructorShouldThrowExceptionWhenPriceIsNull() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_itemsId, null, _period)); // SUT

        assertEquals("Price is required for a direct sale", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenListOfItemIdIsNull() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(null, _priceDouble, _period)); // SUT

        assertEquals("ItemId is required for a direct sale", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenItemIdIsNull() {
        //arrange
        ItemId nullItemId = null;

        //act
        _itemsId.add(_itemIdDouble);
        _itemsId.add(nullItemId);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DirectSale(_itemsId, _priceDouble, _period));

        //assert
        assertEquals("Items cannot contain null elements.", ex.getMessage());
    }

    @Test
    void constructorShouldThrowWhenTimeLimitIsNegative() {

        // Arrange
        Period negativeLimit = Period.ofMonths(-3);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_itemsId, _priceDouble, negativeLimit)); // SUT

        assertEquals("Time limit cannot be negative", ex.getMessage());
    }

    @Test
    void shouldReturnIdentity() {
        //arrange
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period);

        //act
        DirectSaleId result = directSale.identity();

        //assert
        assertNotNull(result);
        assertTrue(result instanceof DirectSaleId);

    }

    @Test
    void shouldReturnTrueWhenSameIdentity() {
        //arrange
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period);

        //act
        boolean result = directSale.equals(directSale);

        //assert
        assertTrue(result);
    }


    @Test
    void shouldReturnFalseWhenDifferentIdentities(){
        //arrange
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, _period);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _period);

        //act
        boolean result = directSale1.equals(directSale2);

        //assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        //arrange
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period);

        //assert
        assertFalse(directSale.sameAs(null));
    }

    @Test
    void shouldReturnFalseWhenObjectIsDifferentType() {
        //arrange
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period);

        boolean result = directSale.equals(_itemIdDouble);

        //assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenSameFields() {
        //arrange
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, _period);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _period);

        //act
        boolean result = directSale1.sameAs(directSale2);

        //assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentItems() {
        //arrange
        ItemId itemIdDouble2 = mock(ItemId.class);
        List<ItemId> itemsId2 = new ArrayList<>();
        itemsId2.add(itemIdDouble2);

        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, _period);
        DirectSale directSale2 = new DirectSale(itemsId2, _priceDouble, _period);

        //act
        boolean result = directSale1.sameAs(directSale2);

        //assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentPrice() {
        //arrange
        Price pricedouble2 = mock(Price.class);

        DirectSale directSale1 = new DirectSale(_itemsId, pricedouble2, _period);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _period);

        //act
        boolean result = directSale1.sameAs(directSale2);

        //assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentTime() {
        //arrange
        Period timeDouble = mock(Period.class);

        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, timeDouble);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _period);

        //act
        boolean result = directSale1.sameAs(directSale2);

        //assert
        assertFalse(result);
    }

}