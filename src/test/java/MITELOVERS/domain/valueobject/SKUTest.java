package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SKUTest {

    @Test
    void constructorShouldGenerateSKUAutomatically() {
        // SUT
        SKU sut = new SKU();

        // Assert
        assertNotNull(sut);
        assertNotNull(sut.getValue());
    }

    @Test
    void generatedSKUShouldHaveCorrectLength() {
        // SUT
        SKU sut = new SKU();

        // Assert
        assertEquals(10, sut.getValue().length());
    }

    @Test
    void generatedSKUShouldMatchExpectedFormat() {
        // SUT
        SKU sut = new SKU();

        // Assert
        assertTrue(sut.getValue().matches("^[A-F0-9]{10}$"));
    }

    @Test
    void generatedSKUShouldBeUppercase() {
        // SUT
        SKU sut = new SKU();

        // Assert
        assertEquals(sut.getValue(), sut.getValue().toUpperCase());
    }

    @Test
    void SKUShouldBeEqualToItself() {
        // SUT
        SKU sut = new SKU();

        // Act
        boolean result = sut.equals(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void SKUShouldNotBeEqualToNull() {
        // SUT
        SKU sut = new SKU();

        // Act
        boolean result = sut.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void SKUShouldNotBeEqualToDifferentType() {
        // SUT
        SKU sut = new SKU();

        // Act
        boolean result = sut.equals("NOT_A_SKU");

        // Assert
        assertFalse(result);
    }

    @Test
    void differentGeneratedSKUsShouldNormallyNotBeEqual() {
        // SUT
        SKU sut = new SKU();
        SKU otherSku = new SKU();

        // Act
        boolean result = sut.equals(otherSku);

        // Assert
        assertFalse(result);
    }

    @Test
    void toStringShouldReturnSKUValue() {
        // SUT
        SKU sut = new SKU();

        // Act
        String result = sut.toString();

        // Assert
        assertEquals(sut.getValue(), result);
    }

    @Test
    void hashCodeShouldBeStableForSameObject() {
        // SUT
        SKU sut = new SKU();

        // Act
        int firstHash = sut.hashCode();
        int secondHash = sut.hashCode();

        // Assert
        assertEquals(firstHash, secondHash);
    }

    @Test
    void hashCodeShouldBeEqualForSameReference() {
        // Arrange
        SKU sut = new SKU();
        SKU sameReference = sut;

        // Act
        int firstHash = sut.hashCode();
        int secondHash = sameReference.hashCode();

        // Assert
        assertEquals(firstHash, secondHash);
    }

    //--------------------------------------
    // Tests for the rehydration constructor
    //--------------------------------------

    @Test
    void rehydrationConstructorShouldStoreGivenValue() {
        String value = "ABCDEF1234";
        SKU sut = new SKU(value);
        assertEquals(value, sut.getValue());
    }

    @Test
    void rehydrationConstructorShouldRejectInvalidFormat() {
        String invalid = "invalid_sku";
        assertThrows(IllegalArgumentException.class, () -> new SKU(invalid));
    }

    @Test
    void rehydratedSKUShouldBeEqualToOriginalValue() {
        String value = "A1B2C3D4E5";
        SKU sut = new SKU(value);
        SKU other = new SKU(value);
        assertEquals(sut, other);
    }
}
