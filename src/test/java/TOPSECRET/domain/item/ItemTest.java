package TOPSECRET.domain.item;

import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemTest {

    private EditionId editionIdDouble;
    private Condition conditionDouble;
    private Description descriptionDouble;

    @BeforeEach
    void setUp() {
        editionIdDouble = mock(EditionId.class);
        conditionDouble = Condition.GOOD;
        descriptionDouble = mock(Description.class);
    }

    // ------------------------------------------------------------
    // Creation
    // ------------------------------------------------------------

    @Test
    void constructorValidArgumentsCreatesItemWithAssignedValues() {
        // Arrange

        // Act
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Assert
        assertSame(editionIdDouble, sut.get_editionId());
        assertEquals(conditionDouble, sut.get_condition());
        assertSame(descriptionDouble, sut.get_description());
    }

    @Test
    void constructorValidArgumentsGeneratesIdentity() {
        // Arrange

        // Act
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Assert
        assertNotNull(sut.identity());
    }

    @Test
    void constructorValidArgumentsSetsDefaultSaleStatusNotOnSale() {
        // Arrange

        // Act
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Assert
        assertEquals(SaleStatus.NotOnSale, sut.get_saleStatus());
    }

    // ------------------------------------------------------------
    // startAuction
    // ------------------------------------------------------------

    @Test
    void markAsAuctionItemNotOnSaleChangesStatusToOnAuction() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        sut.markAsAuction();

        // Assert
        assertEquals(SaleStatus.OnAuction, sut.get_saleStatus());
    }

    @Test
    void markAsAuctionItemAlreadyOnAuctionThrowsIllegalStateException() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
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
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
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
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        sut.markAsDirectSale();

        // Assert
        assertEquals(SaleStatus.OnDirectSale, sut.get_saleStatus());
    }

    @Test
    void markAsDirectSaleItemAlreadyOnDirectSaleThrowsIllegalStateException() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
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
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
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
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        sut.markAsAuction();

        // Act
        sut.markAsSold();

        // Assert
        assertEquals(SaleStatus.Sold, sut.get_saleStatus());
    }

    @Test
    void markAsSoldItemOnDirectSaleChangesStatusToSold() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        sut.markAsDirectSale();

        // Act
        sut.markAsSold();

        // Assert
        assertEquals(SaleStatus.Sold, sut.get_saleStatus());
    }

    @Test
    void markAsSoldItemNotOnSaleThrowsIllegalStateException() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

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
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        EditionId result = sut.get_editionId();

        // Assert
        assertSame(editionIdDouble, result);
    }

    @Test
    void getConditionExistingItemReturnsAssignedCondition() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        Condition result = sut.get_condition();

        // Assert
        assertEquals(conditionDouble, result);
    }

    @Test
    void getDescriptionExistingItemReturnsAssignedDescription() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        Description result = sut.get_description();

        // Assert
        assertSame(descriptionDouble, result);
    }

    @Test
    void getSaleStatusNewItemReturnsNotOnSale() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        SaleStatus result = sut.get_saleStatus();

        // Assert
        assertEquals(SaleStatus.NotOnSale, result);
    }

    @Test
    void getSaleStatusItemOnAuctionReturnsOnAuction() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        sut.markAsAuction();

        // Act
        SaleStatus result = sut.get_saleStatus();

        // Assert
        assertEquals(SaleStatus.OnAuction, result);
    }

    // ------------------------------------------------------------
    // identity
    // ------------------------------------------------------------

    @Test
    void identityExistingItemReturnsNonNullItemId() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        ItemId result = sut.identity();

        // Assert
        assertNotNull(result);
    }

    // ------------------------------------------------------------
    // sameAs
    // ------------------------------------------------------------

    @Test
    void sameAsSameObjectReturnsTrue() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.sameAs(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsDifferentItemReturnsFalse() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        Item otherItem = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.sameAs(otherItem);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

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
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.equals(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsDifferentTypeReturnsFalse() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.equals("not-an-item");

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsDifferentItemsWithSameAttributesReturnsFalse() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        Item otherItem = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.equals(otherItem);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameObjectReturnsSameHashCode() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        int firstHash = sut.hashCode();
        int secondHash = sut.hashCode();

        // Assert
        assertEquals(firstHash, secondHash);
    }

    @Test
    void hashCodeDifferentItemsReturnsDifferentHashCodes() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        Item otherItem = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        int sutHash = sut.hashCode();
        int otherHash = otherItem.hashCode();

        // Assert
        assertNotEquals(sutHash, otherHash);
    }
}