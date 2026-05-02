package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemIdTest {

    @Test
    void constructorShouldCreateItemId() {
        // SUT
        ItemId sut = new ItemId();

        // Assert
        assertNotNull(sut);
    }

    @Test
    void getSKUShouldReturnNonNullSku() {
        // SUT
        ItemId sut = new ItemId();

        // Act
        SKU result = sut.getSku();

        // Assert
        assertNotNull(result);
    }

    @Test
    void equalsSameObjectShouldReturnTrue() {
        // SUT
        ItemId sut = new ItemId();

        // Act
        boolean result = sut.equals(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsNullShouldReturnFalse() {
        // SUT
        ItemId sut = new ItemId();

        // Act
        boolean result = sut.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsDifferentTypeShouldReturnFalse() {
        // SUT
        ItemId sut = new ItemId();

        // Act
        boolean result = sut.equals("differentType");

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsDifferentItemIdsShouldReturnFalse() {
        // SUT
        ItemId sut = new ItemId();
        ItemId otherItemId = new ItemId();

        // Act
        boolean result = sut.equals(otherItemId);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameObjectShouldReturnSameHashCode() {
        // SUT
        ItemId sut = new ItemId();

        // Act
        int firstHash = sut.hashCode();
        int secondHash = sut.hashCode();

        // Assert
        assertEquals(firstHash, secondHash);
    }

    @Test
    void toStringShouldReturnNonNullString() {
        // SUT
        ItemId sut = new ItemId();

        // Act
        String result = sut.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    void toStringShouldMatchSkuToString() {
        // SUT
        ItemId sut = new ItemId();

        // Act
        String result = sut.toString();

        // Assert
        assertEquals(sut.getSku().toString(), result);
    }

    // --------------------------------------
    // Tests for the rehydration constructor
    // --------------------------------------

    @Test
    void rehydrationConstructorShouldStoreGivenSkuValue() {
        String value = "ABCDEF1234"; // valid SKU
        ItemId sut = new ItemId(value);
        assertEquals(value, sut.getSku().getValue());
    }

    @Test
    void rehydrationConstructorShouldRejectInvalidSkuValue() {
        String invalid = "invalid_sku";
        assertThrows(IllegalArgumentException.class, () -> new ItemId(invalid));
    }

    @Test
    void rehydratedItemIdsWithSameValueShouldBeEqual() {
        String value = "A1B2C3D4E5";
        ItemId sut = new ItemId(value);
        ItemId other = new ItemId(value);
        assertEquals(sut, other);
    }
}
