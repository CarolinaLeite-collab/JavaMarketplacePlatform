package TOPSECRET.domain.directsale;

import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleTest {

    private List<Item> _items;
    private DirectSaleId _dsId;
    private Item _itemDouble;
    private Price _priceDouble;
    private Period _period;

    @BeforeEach
    void setUp() {

        _items = new ArrayList<>();
        _itemDouble = mock(Item.class);
        when(_itemDouble.get_saleStatus()).thenReturn(SaleStatus.NotOnSale);
        _items.add(_itemDouble);
        _priceDouble = mock(Price.class);
        _period = Period.ofMonths(3);
    }

    @Test
    void constructorShouldBuildDirectSaleWithTimeLimit() {

        // Act
        DirectSale directSale = new DirectSale(_items, _priceDouble, _period); // SUT

        // Assert
        assertEquals(_items, directSale.getItems());
        assertEquals(_priceDouble, directSale.getPrice());
        assertEquals(_period, directSale.getTimeLimit());
    }

    @Test
    void constructorShouldBuildDirectSaleWithoutTimeLimit() {

        // Act
        DirectSale directSale = new DirectSale(_items, _priceDouble, null); // SUT

        // Assert
        assertEquals(_items, directSale.getItems());
        assertEquals(_priceDouble, directSale.getPrice());
        assertNull(directSale.getTimeLimit());
    }

    @Test
    void constructorShouldThrowExceptionWhenPriceIsNull() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_items, null, _period)); // SUT

        assertEquals("Price is required for a direct sale", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenItemIsNull() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(null, _priceDouble, _period)); // SUT

        assertEquals("Item is required for a direct sale", ex.getMessage());
    }

    @Test
    void constructorShouldThrowWhenTimeLimitIsNegative() {

        // Arrange
        Period negativeLimit = Period.ofMonths(-3);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_items, _priceDouble, negativeLimit)); // SUT

        assertEquals("Time limit cannot be negative", ex.getMessage());
    }

    @Test
    void shouldReturnIdentity() {
        //arrange
        DirectSale directSale = new DirectSale(_items, _priceDouble, _period);

        //act
        DirectSaleId result = directSale.identity();

        //assert
        assertNotNull(result);
        assertTrue(result instanceof DirectSaleId);

    }

    @Test
    void shouldReturnTrueWhenSameIdentity() {
        //arrange
        DirectSale directSale = new DirectSale(_items, _priceDouble, _period);

        //act
        boolean result = directSale.equals(directSale);

        //assert
        assertTrue(result);
    }


    @Test
    void shouldReturnFalseWhenDifferentIdentities(){
        //arrange
        DirectSale directSale1 = new DirectSale(_items, _priceDouble, _period);
        DirectSale directSale2 = new DirectSale(_items, _priceDouble, _period);

        //act
        boolean result = directSale1.equals(directSale2);

        //assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        //arrange
        DirectSale directSale = new DirectSale(_items, _priceDouble, _period);

        //assert
        assertFalse(directSale.sameAs(null));
    }

    @Test
    void shouldReturnFalseWhenObjectIsDifferentType() {
        //arrange
        DirectSale directSale = new DirectSale(_items, _priceDouble, _period);

        boolean result = directSale.equals(_itemDouble);

        //assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenSameFields() {
        //arrange
        DirectSale directSale1 = new DirectSale(_items, _priceDouble, _period);
        DirectSale directSale2 = new DirectSale(_items, _priceDouble, _period);

        //act
        boolean result = directSale1.sameAs(directSale2);

        //assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentItems() {
        //arrange
        Item itemdouble2 = mock(Item.class);
        when(itemdouble2.get_saleStatus()).thenReturn(SaleStatus.NotOnSale);
        List<Item> items2 = new ArrayList<>();
        items2.add(itemdouble2);

        DirectSale directSale1 = new DirectSale(_items, _priceDouble, _period);
        DirectSale directSale2 = new DirectSale(items2, _priceDouble, _period);

        //act
        boolean result = directSale1.sameAs(directSale2);

        //assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentPrice() {
        //arrange
        Price pricedouble2 = mock(Price.class);

        DirectSale directSale1 = new DirectSale(_items, pricedouble2, _period);
        DirectSale directSale2 = new DirectSale(_items, _priceDouble, _period);

        //act
        boolean result = directSale1.sameAs(directSale2);

        //assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentTime() {
        //arrange
        Period timeDouble = mock(Period.class);

        DirectSale directSale1 = new DirectSale(_items, _priceDouble, timeDouble);
        DirectSale directSale2 = new DirectSale(_items, _priceDouble, _period);

        //act
        boolean result = directSale1.sameAs(directSale2);

        //assert
        assertFalse(result);
    }

}