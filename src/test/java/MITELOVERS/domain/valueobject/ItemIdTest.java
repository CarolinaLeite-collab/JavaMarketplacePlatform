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

    @Test
    void constructorWithStringShouldCreateItemIdSuccessfully() {
        // Arrange
        String validSku = "ABCDEF1234";

        // SUT & Act
        ItemId sut = new ItemId(validSku);

        // Assert
        assertNotNull(sut);
        assertEquals(validSku, sut.toString());
    }

    @Test
    void constructorWithStringShouldThrowWhenInvalidSku() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ItemId("INVALID!!!"));
    }

    @Test
    void constructorWithStringShouldThrowWhenNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ItemId(null));
    }

    @Test
    void itemIdsWithSameSkuStringShouldBeEqual() {
        // Arrange
        String validSku = "ABCDEF1234";

        // SUT
        ItemId id1 = new ItemId(validSku);
        ItemId id2 = new ItemId(validSku);

        // Assert
        assertEquals(id1, id2);
    }

}
