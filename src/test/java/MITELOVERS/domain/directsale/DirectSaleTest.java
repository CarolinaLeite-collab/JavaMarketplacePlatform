package MITELOVERS.domain.directsale;

import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DirectSaleTest {

    private List<ItemId> _itemsId;
    private DirectSaleId _dsId;
    private ItemId _itemIdDouble;
    private Price _priceDouble;
    private Duration _timeLimit;
    private Instant _creationDate;

    @BeforeEach
    void setUp() {

        _itemsId = new ArrayList<>();
        _itemIdDouble = mock(ItemId.class);
        _itemsId.add(_itemIdDouble);
        _priceDouble = mock(Price.class);
        _timeLimit = Duration.ofDays(3);
        _creationDate = Instant.parse("2024-01-01T10:00:00Z");
    }

    @Test
    void constructorShouldRebuildDirectSaleWithDirectSaleID() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_dsId, _itemsId, _priceDouble, _timeLimit, _creationDate);

        //Act & Assert
        assertEquals(_dsId, directSale.identity());

    }

    @Test
    void constructorShouldBuildDirectSaleWithTimeLimit() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _timeLimit);

        //Act & Assert
        assertEquals(_itemsId, directSale.getItemsId());
        assertEquals(_priceDouble, directSale.getPrice());
        assertEquals(_timeLimit, directSale.getTimeLimit());
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
                () -> new DirectSale(_itemsId, null, _timeLimit));

        //Assert
        assertEquals("Price is required for a direct sale", ex.getMessage());

    }

    @Test
    void constructorShouldThrowExceptionWhenListOfItemIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(null, _priceDouble, _timeLimit)); // SUT

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

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DirectSale(_itemsId, _priceDouble, _timeLimit));

        //Assert
        assertEquals("Items cannot contain null elements.", ex.getMessage());
    }

    @Test
    void constructorShouldThrowWhenTimeLimitIsNegative() {
        //Arrange
        Duration negativeLimit = Duration.ofDays(-3);

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
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _timeLimit);

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
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _timeLimit);

        //Act
        boolean result = directSale.equals(directSale);

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentIdentities(){
        //Arrange
        //SUT
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, _timeLimit);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _timeLimit);

        //Act
        boolean result = directSale1.equals(directSale2);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _timeLimit);

        //Assert
        assertFalse(directSale.sameAs(null));
    }

    @Test
    void shouldReturnFalseWhenObjectIsDifferentType() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsId, _priceDouble, _timeLimit);

        //Act
        boolean result = directSale.equals(_itemIdDouble);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenSameFields() {
        //Arrange
        //SUT
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, _timeLimit);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _timeLimit);

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
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, _timeLimit);
        DirectSale directSale2 = new DirectSale(itemsId2, _priceDouble, _timeLimit);

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
        DirectSale directSale1 = new DirectSale(_itemsId, pricedouble2, _timeLimit);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _timeLimit);

        //Act
        boolean result = directSale1.sameAs(directSale2);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentTime() {
        //Arrange
        Duration timeDouble = Duration.ofDays(5);

        //SUT
        DirectSale directSale1 = new DirectSale(_itemsId, _priceDouble, timeDouble);
        DirectSale directSale2 = new DirectSale(_itemsId, _priceDouble, _timeLimit);

        //Act
        boolean result = directSale1.sameAs(directSale2);

        //Assert
        assertFalse(result);
    }
}
