package MITELOVERS.domain.directsale;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DirectSaleTest {

    private List<ItemId> _itemsIdDouble;
    private DirectSaleId _dsIdDouble;
    private UserId _sellerIdDouble;
    private ItemId _itemIdDouble;
    private Price _priceDouble;
    private Duration _timeLimit;
    private Instant _creationDate;
    private DirectSaleStatus _saleStatus;

    @BeforeEach
    void setUp() {

        _dsIdDouble = mock(DirectSaleId.class);
        _itemsIdDouble = new ArrayList<>();
        _sellerIdDouble = mock(UserId.class);
        _itemIdDouble = mock(ItemId.class);
        _itemsIdDouble.add(_itemIdDouble);
        _priceDouble = mock(Price.class);
        _timeLimit = Duration.ofDays(3);
        _creationDate = Instant.parse("2024-01-01T10:00:00Z");
        _saleStatus = DirectSaleStatus.ACTIVE;
    }

    @Test
    void constructorShouldRebuildDirectSaleWithDirectSaleID() {
        //Arrange

        //SUT
        DirectSale directSale = new DirectSale(_dsIdDouble, _itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit, _creationDate, _saleStatus);

        //Act & Assert
        assertEquals(_dsIdDouble, directSale.identity());

    }

    @Test
    void constructorShouldBuildDirectSaleWithTimeLimit() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);

        //Act & Assert
        assertEquals(_itemsIdDouble, directSale.getItemsId());
        assertEquals(_priceDouble, directSale.getPrice());
        assertEquals(_timeLimit, directSale.getTimeLimit());
        assertThrows(UnsupportedOperationException.class,
                () -> directSale.getItemsId().add(mock(ItemId.class)));
    }

    @Test
    void constructorShouldBuildDirectSaleWithoutTimeLimit() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, null);

        //Act & Assert
        assertEquals(_itemsIdDouble, directSale.getItemsId());
        assertEquals(_priceDouble, directSale.getPrice());
        assertNull(directSale.getTimeLimit());
    }

    @Test
    void constructorShouldThrowExceptionWhenPriceIsNull() {
        //Act
        //SUT
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_itemsIdDouble, _sellerIdDouble, null, _timeLimit));

        //Assert
        assertEquals("Price is required for a direct sale", ex.getMessage());

    }

    @Test
    void constructorShouldThrowExceptionWhenListOfItemIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(null, _sellerIdDouble, _priceDouble, _timeLimit)); // SUT

        //Assert
        assertEquals("ItemId is required for a direct sale", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenItemIdIsNull() {
        //Arrange
        List<ItemId> listWithNull = new ArrayList<>();

        //Act
        listWithNull.add(mock(ItemId.class));
        listWithNull.add(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(listWithNull, _sellerIdDouble, _priceDouble, _timeLimit)
        );

        //Assert
        assertEquals("Items cannot contain null elements.", ex.getMessage());
    }

    @Test
    void constructorShouldThrowWhenTimeLimitIsNegative() {
        //Arrange
        Duration negativeLimit = Duration.ofDays(-3);

        //Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, negativeLimit)); // SUT

        //Assert
        assertEquals("Time limit cannot be negative", ex.getMessage());
    }

    @Test
    void constructorShouldThrowWhenDuplicateItemsProvided() {

        ItemId id = mock(ItemId.class);
        List<ItemId> duplicates = List.of(id, id);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(duplicates, _sellerIdDouble, _priceDouble, _timeLimit)
        );

        assertEquals("DirectSale cannot contain duplicate items.", ex.getMessage());
    }

    @Test
    void shouldReturnIdentity() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);

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
        DirectSale directSale = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);

        //Act
        boolean result = directSale.equals(directSale);

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentIdentities(){
        //Arrange
        //SUT
        DirectSale directSale1 = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);
        DirectSale directSale2 = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);

        //Act
        boolean result = directSale1.equals(directSale2);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);

        //Assert
        assertFalse(directSale.sameAs(null));
    }

    @Test
    void shouldReturnFalseWhenObjectIsDifferentType() {
        //Arrange
        //SUT
        DirectSale directSale = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);

        //Act
        boolean result = directSale.equals(_itemIdDouble);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenSameFields() {
        //Arrange
        //SUT
        DirectSale directSale1 = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);
        DirectSale directSale2 = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);

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
        DirectSale directSale1 = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);
        DirectSale directSale2 = new DirectSale(itemsId2, _sellerIdDouble, _priceDouble, _timeLimit);

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
        DirectSale directSale1 = new DirectSale(_itemsIdDouble, _sellerIdDouble, pricedouble2, _timeLimit);
        DirectSale directSale2 = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);

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
        DirectSale directSale1 = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, timeDouble);
        DirectSale directSale2 = new DirectSale(_itemsIdDouble, _sellerIdDouble, _priceDouble, _timeLimit);

        //Act
        boolean result = directSale1.sameAs(directSale2);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnCreationDate() {
        //Arrange
        DirectSaleId directSaleId = new DirectSaleId("DS-ABCDEF12");
        Instant creationDate = Instant.parse("2024-01-01T10:00:00Z");

        DirectSale directSale = new DirectSale(
                directSaleId,
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                _timeLimit,
                creationDate,
                _saleStatus
        );

        //Act
        Instant result = directSale.getCreationDate();

        //Assert
        assertEquals(creationDate, result);
    }

    @Test
    void shouldReturnSellerId() {
        //Arrange
        DirectSaleId directSaleId = new DirectSaleId("DS-ABCDEF12");
        Instant creationDate = Instant.parse("2024-01-01T10:00:00Z");

        DirectSale directSale = new DirectSale(
                directSaleId,
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                _timeLimit,
                creationDate,
                _saleStatus
        );

        //Act

        UserId result = directSale.getSellerId();

        //Assert
        assertEquals(_sellerIdDouble, result);

    }

    @Test
    void shouldReturnStatus() {
        //Arrange
        DirectSaleId directSaleId = new DirectSaleId("DS-ABCDEF12");
        Instant creationDate = Instant.parse("2024-01-01T10:00:00Z");

        DirectSale directSale = new DirectSale(
                directSaleId,
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                _timeLimit,
                creationDate,
                _saleStatus
        );

        //Act

        DirectSaleStatus result = directSale.getDSStatus();

        //Assert
        assertEquals(_saleStatus, result);

    }

    @Test
    void shouldBeMarkedAsCompleteAfterBeingMarkedCompleted() {
        // Arrange
        DirectSale directSale = new DirectSale(
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                _timeLimit
        );

        // Act
        directSale.markAsCompleted();

        // Assert
        assertEquals(DirectSaleStatus.COMPLETED, directSale.getDSStatus());
    }

    @Test
    void shouldThrowIfDirectSaleIsAlreadyCompleted() {
        // Arrange
        DirectSale directSale = new DirectSale(
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                _timeLimit
        );

        // Act
        directSale.markAsCompleted();

        // Assert
        assertThrows(
                IllegalStateException.class,
                () -> directSale.markAsCompleted()
        );
    }

    @Test
    void shouldBeMarkedAsExpiredAfterBeingMarkedExpired() {
        // Arrange
        Instant creationDate = Instant.now().minus(Duration.ofDays(10));

        DirectSale directSale = new DirectSale(
                _dsIdDouble,
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                Duration.ofDays(3),
                creationDate,
                DirectSaleStatus.ACTIVE
        );

        // Act
        directSale.markAsExpired();

        // Assert
        assertEquals(DirectSaleStatus.EXPIRED, directSale.getDSStatus());
    }

    @Test
    void shouldThrowIfDirectSaleIsAlreadyExpired() {
        // Arrange
        Instant creationDate = Instant.now().minus(Duration.ofDays(10));

        DirectSale directSale = new DirectSale(
                _dsIdDouble,
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                Duration.ofDays(3),
                creationDate,
                DirectSaleStatus.ACTIVE
        );

        // Act
        directSale.markAsExpired();

        // Assert
        assertThrows(
                IllegalStateException.class,
                () -> directSale.markAsExpired()
        );
    }

    @Test
    void shouldThrowIfDirectSaleIsStillWithinTimeLimit() {
        // Arrange
        DirectSale directSale = new DirectSale(
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                _timeLimit
        );

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> directSale.markAsExpired()
        );
    }

    @Test
    void shouldReturnNullEndDateWhenTimeLimitIsNull() {
        // Arrange
        DirectSale directSale = new DirectSale(
                _dsIdDouble,
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                null,
                _creationDate,
                _saleStatus
        );

        // Act
        Instant result = directSale.getEndDate();

        // Assert
        assertNull(result);
    }

    @Test
    void shouldReturnEndDateWhenTimeLimitExists() {
        // Arrange
        DirectSale directSale = new DirectSale(
                _dsIdDouble,
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                _timeLimit,
                _creationDate,
                _saleStatus
        );

        Instant expected = _creationDate.plus(_timeLimit);

        // Act
        Instant result = directSale.getEndDate();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowIfDirectSaleHasNoTimeLimit() {
        // Arrange
        DirectSale directSale = new DirectSale(
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                null
        );

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> directSale.markAsExpired()
        );
    }

    @Test
    void shouldReturnFalseWhenDirectSaleHasNoTimeLimit() {
        // Arrange
        DirectSale directSale = new DirectSale(
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                null
        );

        // Act
        boolean result = directSale.isExpired();

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDirectSaleIsStillWithinTimeLimit() {
        // Arrange
        DirectSale directSale = new DirectSale(
                _dsIdDouble,
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                Duration.ofDays(10),
                Instant.now(),
                DirectSaleStatus.ACTIVE
        );

        // Act
        boolean result = directSale.isExpired();

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenDirectSaleHasPassedEndDate() {
        // Arrange
        DirectSale directSale = new DirectSale(
                _dsIdDouble,
                _itemsIdDouble,
                _sellerIdDouble,
                _priceDouble,
                Duration.ofDays(3),
                Instant.now().minus(Duration.ofDays(10)),
                DirectSaleStatus.ACTIVE
        );

        // Act
        boolean result = directSale.isExpired();

        // Assert
        assertTrue(result);
    }
}
