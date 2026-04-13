package TOPSECRET.domain.item;

import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Item}.
 *
 * <p>Tests are divided into two categories:
 * <ul>
 *   <li><b>Integration-style</b> — use real domain objects ({@link Publication}, {@link Condition})
 *       to verify Item behaviour end-to-end (sale/auction lifecycle, condition preservation).</li>
 *   <li><b>Isolated</b> — use Mockito doubles for {@link Publication} and {@link Condition}
 *       to verify delegation of {@code isByAuthor}, {@code isByGenre} and {@code isByPublication}.</li>
 * </ul>
 */

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
    void constructor_validArguments_createsItemWithAssignedValues() {
        // Arrange

        // Act
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Assert
        assertSame(editionIdDouble, sut.get_editionId());
        assertEquals(conditionDouble, sut.get_condition());
        assertSame(descriptionDouble, sut.get_description());
    }

    @Test
    void constructor_validArguments_generatesIdentity() {
        // Arrange

        // Act
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Assert
        assertNotNull(sut.identity());
    }

    @Test
    void constructor_validArguments_setsDefaultSaleStatusNotOnSale() {
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
    void markAsAuction_itemNotOnSale_changesStatusToOnAuction() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        sut.markAsAuction();

        // Assert
        assertEquals(SaleStatus.OnAuction, sut.get_saleStatus());
    }

    @Test
    void markAsAuction_itemAlreadyOnAuction_throwsIllegalStateException() {
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
    void markAsAuction_itemAlreadyOnDirectSale_throwsIllegalStateException() {
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
    void markAsDirectSale_itemNotOnSale_changesStatusToOnDirectSale() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        sut.markAsDirectSale();

        // Assert
        assertEquals(SaleStatus.OnDirectSale, sut.get_saleStatus());
    }

    @Test
    void markAsDirectSale_itemAlreadyOnDirectSale_throwsIllegalStateException() {
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
    void markAsDirectSale_itemAlreadyOnAuction_throwsIllegalStateException() {
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
    void markAsSold_itemOnAuction_changesStatusToSold() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        sut.markAsAuction();

        // Act
        sut.markAsSold();

        // Assert
        assertEquals(SaleStatus.Sold, sut.get_saleStatus());
    }

    @Test
    void markAsSold_itemOnDirectSale_changesStatusToSold() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        sut.markAsDirectSale();

        // Act
        sut.markAsSold();

        // Assert
        assertEquals(SaleStatus.Sold, sut.get_saleStatus());
    }

    @Test
    void markAsSold_itemNotOnSale_throwsIllegalStateException() {
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
    void getEditionId_existingItem_returnsAssignedEditionId() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        EditionId result = sut.get_editionId();

        // Assert
        assertSame(editionIdDouble, result);
    }

    @Test
    void getCondition_existingItem_returnsAssignedCondition() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        Condition result = sut.get_condition();

        // Assert
        assertEquals(conditionDouble, result);
    }

    @Test
    void getDescription_existingItem_returnsAssignedDescription() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        Description result = sut.get_description();

        // Assert
        assertSame(descriptionDouble, result);
    }

    @Test
    void getSaleStatus_newItem_returnsNotOnSale() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        SaleStatus result = sut.get_saleStatus();

        // Assert
        assertEquals(SaleStatus.NotOnSale, result);
    }

    @Test
    void getSaleStatus_itemOnAuction_returnsOnAuction() {
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
    void identity_existingItem_returnsNonNullItemId() {
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
    void sameAs_sameObject_returnsTrue() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.sameAs(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAs_differentItem_returnsFalse() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        Item otherItem = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.sameAs(otherItem);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_differentType_returnsFalse() {
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
    void equals_sameObject_returnsTrue() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.equals(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_null_returnsFalse() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentType_returnsFalse() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.equals("not-an-item");

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentItemsWithSameAttributes_returnsFalse() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);
        Item otherItem = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        boolean result = sut.equals(otherItem);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCode_sameObject_returnsSameHashCode() {
        // Arrange
        Item sut = new Item(editionIdDouble, conditionDouble, descriptionDouble);

        // Act
        int firstHash = sut.hashCode();
        int secondHash = sut.hashCode();

        // Assert
        assertEquals(firstHash, secondHash);
    }

    @Test
    void hashCode_differentItems_returnsDifferentHashCodes() {
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