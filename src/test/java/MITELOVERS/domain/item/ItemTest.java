package MITELOVERS.domain.item;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemTest {

    private EditionId _editionIdDouble;
    private Condition _conditionDouble;
    private Description _descriptionDouble;
    private Name _nameDouble;

    @BeforeEach
    void setUp() {
        _editionIdDouble = mock(EditionId.class);
        _conditionDouble = Condition.GOOD;
        _descriptionDouble = mock(Description.class);
        _nameDouble = mock(Name.class);
    }

    // ------------------------------------------------------------
    // Creation
    // ------------------------------------------------------------

    @Test
    void constructorValidArgumentsCreatesNewItemWithAssignedValues() {
        // Arrange

        // Act
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Assert
        assertEquals(_editionIdDouble, sut.getEditionId());
        assertEquals(_conditionDouble, sut.getCondition());
        assertEquals(_descriptionDouble, sut.getDescription());
        assertEquals(_nameDouble, sut.getName());
    }

    @Test
    void reconstructionConstructorValidArgumentsCreatesItemWithGivenIdentity() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        // Act
        Item sut = new Item(itemIdDouble, _editionIdDouble, _conditionDouble, _descriptionDouble, SaleStatus.OnDirectSale, _nameDouble);

        // Assert
        assertEquals(itemIdDouble, sut.identity());
        assertEquals(_editionIdDouble, sut.getEditionId());
        assertEquals(_conditionDouble, sut.getCondition());
        assertEquals(_descriptionDouble, sut.getDescription());
        assertEquals(SaleStatus.OnDirectSale, sut.getSaleStatus());
        assertEquals(_nameDouble, sut.getName());
    }

    @Test
    void constructorValidArgumentsGeneratesIdentity() {
        // Arrange

        // Act
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Assert
        assertNotNull(sut.identity());
    }

    @Test
    void constructorValidArgumentsSetsDefaultSaleStatusNotOnSale() {
        // Arrange

        // Act
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Assert
        assertEquals(SaleStatus.NotOnSale, sut.getSaleStatus());
    }

    // ------------------------------------------------------------
    // startAuction
    // ------------------------------------------------------------

    @Test
    void markAsAuctionItemNotOnSaleChangesStatusToOnAuction() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        sut.markAsAuction();

        // Assert
        assertEquals(SaleStatus.OnAuction, sut.getSaleStatus());
    }

    @Test
    void markAsAuctionItemAlreadyOnAuctionThrowsIllegalStateException() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        sut.markAsAuction();

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                sut::markAsAuction
        );

        // Assert
        assertEquals("Item is already on sale.", exception.getMessage());
    }

    @Test
    void markAsAuctionItemAlreadyOnDirectSaleThrowsIllegalStateException() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        sut.markAsDirectSale();

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                sut::markAsAuction
        );

        // Assert
        assertEquals("Item is already on sale.", exception.getMessage());
    }

    // ------------------------------------------------------------
    // startDirectSale
    // ------------------------------------------------------------

    @Test
    void markAsDirectSaleItemNotOnSaleChangesStatusToOnDirectSale() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        sut.markAsDirectSale();

        // Assert
        assertEquals(SaleStatus.OnDirectSale, sut.getSaleStatus());
    }

    @Test
    void markAsDirectSaleItemAlreadyOnDirectSaleThrowsIllegalStateException() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        sut.markAsDirectSale();

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                sut::markAsDirectSale
        );

        // Assert
        assertEquals("Item is already on sale.", exception.getMessage());
    }

    @Test
    void markAsDirectSaleItemAlreadyOnAuctionThrowsIllegalStateException() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        sut.markAsAuction();

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                sut::markAsDirectSale
        );

        // Assert
        assertEquals("Item is already on sale.", exception.getMessage());
    }

    // ------------------------------------------------------------
    // markAsSold
    // ------------------------------------------------------------

    @Test
    void markAsSoldItemOnAuctionChangesStatusToSold() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        sut.markAsAuction();

        // Act
        sut.markAsSold();

        // Assert
        assertEquals(SaleStatus.Sold, sut.getSaleStatus());
    }

    @Test
    void markAsSoldItemOnDirectSaleChangesStatusToSold() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        sut.markAsDirectSale();

        // Act
        sut.markAsSold();

        // Assert
        assertEquals(SaleStatus.Sold, sut.getSaleStatus());
    }

    @Test
    void markAsSoldItemNotOnSaleThrowsIllegalStateException() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                sut::markAsSold
        );

        // Assert
        assertEquals("Item is not on sale.", exception.getMessage());
    }

    // ------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------

    @Test
    void getEditionIdExistingItemReturnsAssignedEditionId() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        EditionId result = sut.getEditionId();

        // Assert
        assertSame(_editionIdDouble, result);
    }

    @Test
    void getConditionExistingItemReturnsAssignedCondition() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        Condition result = sut.getCondition();

        // Assert
        assertEquals(_conditionDouble, result);
    }

    @Test
    void getDescriptionExistingItemReturnsAssignedDescription() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        Description result = sut.getDescription();

        // Assert
        assertSame(_descriptionDouble, result);
    }

    @Test
    void getSaleStatusNewItemReturnsNotOnSale() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        SaleStatus result = sut.getSaleStatus();

        // Assert
        assertEquals(SaleStatus.NotOnSale, result);
    }

    @Test
    void getSaleStatusItemOnAuctionReturnsOnAuction() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        sut.markAsAuction();

        // Act
        SaleStatus result = sut.getSaleStatus();

        // Assert
        assertEquals(SaleStatus.OnAuction, result);
    }

    @Test
    void getNameExistingItemReturnsAssignedName() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        Name result = sut.getName();

        // Assert
        assertSame(_nameDouble, result);
    }

    // ------------------------------------------------------------
    // identity
    // ------------------------------------------------------------

    @Test
    void identityExistingItemReturnsNonNullItemId() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        ItemId result = sut.identity();

        // Assert
        assertNotNull(result);
    }
    @Test
    void identityCalledTwiceReturnsSameItemIdInstance() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        ItemId first = sut.identity();
        ItemId second = sut.identity();

        // Assert
        assertSame(first, second);
    }

    // ------------------------------------------------------------
    // sameAs
    // ------------------------------------------------------------

    @Test
    void sameAsSameObjectReturnsTrue() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        boolean result = sut.sameAs(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsDifferentItemReturnsFalse() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        Item otherItem = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        boolean result = sut.sameAs(otherItem);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        boolean result = sut.sameAs("not-an-item");

        // Assert
        assertFalse(result);
    }

    // ------------------------------------------------------------
    // equals and hashCode
    // ------------------------------------------------------------

    @Test
    void equalsSameObjectReturnsTrue() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        boolean result = sut.equals(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        boolean result = sut.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsDifferentTypeReturnsFalse() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        boolean result = sut.equals("not-an-item");

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsDifferentItemsWithSameAttributesReturnsFalse() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        Item otherItem = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        boolean result = sut.equals(otherItem);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsDifferentInstancesWithSameItemIdReturnsTrue() {
        // Arrange
        ItemId sharedItemId = new ItemId();

        Item first = new Item(
                sharedItemId,
                _editionIdDouble,
                _conditionDouble,
                _descriptionDouble,
                SaleStatus.NotOnSale,
                _nameDouble
        );

        Item second = new Item(
                sharedItemId,
                _editionIdDouble,
                _conditionDouble,
                _descriptionDouble,
                SaleStatus.NotOnSale,
                _nameDouble
        );

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    @Test
    void hashCodeSameObjectReturnsSameHashCode() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        int firstHash = sut.hashCode();
        int secondHash = sut.hashCode();

        // Assert
        assertEquals(firstHash, secondHash);
    }

    @Test
    void hashCodeDifferentItemsReturnsDifferentHashCodes() {
        // Arrange
        Item sut = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);
        Item otherItem = new Item(_editionIdDouble, _conditionDouble, _descriptionDouble, _nameDouble);

        // Act
        int sutHash = sut.hashCode();
        int otherHash = otherItem.hashCode();

        // Assert
        assertNotEquals(sutHash, otherHash);
    }
}
