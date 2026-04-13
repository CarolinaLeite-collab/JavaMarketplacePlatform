package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemIdTest {

    @Test
    void constructor_shouldCreateItemId() {
        // Act
        ItemId sut = new ItemId();

        // Assert
        assertNotNull(sut);
    }

    @Test
    void getSku_shouldReturnNonNullSku() {
        // Arrange
        ItemId sut = new ItemId();

        // Act
        SKU result = sut.getSku();

        // Assert
        assertNotNull(result);
    }

    @Test
    void equals_sameObject_shouldReturnTrue() {
        // Arrange
        ItemId sut = new ItemId();

        // Act
        boolean result = sut.equals(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_null_shouldReturnFalse() {
        // Arrange
        ItemId sut = new ItemId();

        // Act
        boolean result = sut.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentType_shouldReturnFalse() {
        // Arrange
        ItemId sut = new ItemId();

        // Act
        boolean result = sut.equals("differentType");

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentItemIds_shouldReturnFalse() {
        // Arrange
        ItemId sut = new ItemId();
        ItemId otherItemId = new ItemId();

        // Act
        boolean result = sut.equals(otherItemId);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCode_sameObject_shouldReturnSameHashCode() {
        // Arrange
        ItemId sut = new ItemId();

        // Act
        int firstHash = sut.hashCode();
        int secondHash = sut.hashCode();

        // Assert
        assertEquals(firstHash, secondHash);
    }

    @Test
    void toString_shouldReturnNonNullString() {
        // Arrange
        ItemId sut = new ItemId();

        // Act
        String result = sut.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    void toString_shouldMatchSkuToString() {
        // Arrange
        ItemId sut = new ItemId();

        // Act
        String result = sut.toString();

        // Assert
        assertEquals(sut.getSku().toString(), result);
    }
}
