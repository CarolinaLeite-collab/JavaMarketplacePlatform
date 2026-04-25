package MITELOVERS.domain.directsale;

import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
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
    void constructorShouldRebuildDirectSaleWithDirectSaleID() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_dsId, _itemsId, _priceDouble, _period);

        //Act & Assert
        assertEquals(_dsId, directSale.identity());

    }

    @Test
    void constructorShouldBuildDirectSaleWithTimeLimit() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period);

        //Act & Assert
        assertEquals(_itemsId, directSale.getItemsId());
        assertEquals(_priceDouble, directSale.getPrice());
        assertEquals(_period, directSale.getTimeLimit());
    }

    @Test
    void constructorShouldBuildDirectSaleWithoutTimeLimit() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, null);

        //Act & Assert
        assertEquals(_itemsId, directSale.getItemsId());
        assertEquals(_priceDouble, directSale.getPrice());
        assertNull(directSale.getTimeLimit());
    }

    @Test
    void constructorShouldThrowExceptionWhenPriceIsNull() {
        //Act
        //SUT
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_itemsId, null, _period));

        //Assert
        assertEquals("Price is required for a direct sale", ex.getMessage());

    }

    @Test
    void constructorShouldThrowExceptionWhenListOfItemIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(null, _priceDouble, _period)); // SUT

        //Assert
        assertEquals("ItemId is required for a direct sale", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenItemIdIsNull() {
        //Arrange
        ItemId nullItemId = null;

        //Act
        _itemsId.add(_itemIdDouble);
        _itemsId.add(nullItemId);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DirectSale(_itemsId, _priceDouble, _period));

        //Assert
        assertEquals("Items cannot contain null elements.", ex.getMessage());
    }

    @Test
    void constructorShouldThrowWhenTimeLimitIsNegative() {
        //Arrange
        Period negativeLimit = Period.ofMonths(-3);

        //Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_itemsId, _priceDouble, negativeLimit)); // SUT

        //Assert
        assertEquals("Time limit cannot be negative", ex.getMessage());
    }

    @Test
    void shouldReturnIdentity() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period);

        //Act
        DirectSaleId result = directSale.identity();

        //Assert
        assertNotNull(result);
        assertTrue(result instanceof DirectSaleId);

    }

    @Test
    void shouldReturnTrueWhenSameIdentity() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period);

        //Act
        boolean result = directSale.equals(directSale);

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentIdentities(){
        //Arrange
        //SUT
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, _period);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _period);

        //Act
        boolean result = directSale1.equals(directSale2);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period);

        //Assert
        assertFalse(directSale.sameAs(null));
    }

    @Test
    void shouldReturnFalseWhenObjectIsDifferentType() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _period);

        //Act
        boolean result = directSale.equals(_itemIdDouble);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenSameFields() {
        //Arrange
        //SUT
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, _period);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _period);

        //Act
        boolean result = directSale1.sameAs(directSale2);

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentItems() {
        //Arrange
        ItemId itemIdDouble2 = mock(ItemId.class);
        List<ItemId> itemsId2 = new ArrayList<>();
        itemsId2.add(itemIdDouble2);

        //SUT
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, _period);
        DirectSale directSale2 = new DirectSale(itemsId2, _priceDouble, _period);

        //Act
        boolean result = directSale1.sameAs(directSale2);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentPrice() {
        //Arrange
        Price pricedouble2 = mock(Price.class);

        //SUT
        DirectSale directSale1 = new DirectSale(_itemsId, pricedouble2, _period);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _period);

        //Act
        boolean result = directSale1.sameAs(directSale2);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentTime() {
        //Arrange
        Period timeDouble = mock(Period.class);

        //SUT
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, timeDouble);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _period);

        //Act
        boolean result = directSale1.sameAs(directSale2);

        //Assert
        assertFalse(result);
    }

}
