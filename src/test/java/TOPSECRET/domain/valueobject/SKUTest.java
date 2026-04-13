package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SKUTest {

    @Test
    void constructorShouldGenerateSkuAutomatically() {
        // Act
        SKU sut = new SKU();

        // Assert
        assertNotNull(sut);
        assertNotNull(sut.getValue());
    }

    @Test
    void generatedSkuShouldHaveCorrectLength() {
        // Act
        SKU sut = new SKU();

        // Assert
        assertEquals(10, sut.getValue().length());
    }

    @Test
    void generatedSkuShouldMatchExpectedFormat() {
        // Act
        SKU sut = new SKU();

        // Assert
        assertTrue(sut.getValue().matches("^[A-F0-9]{10}$"));
    }

    @Test
    void generatedSkuShouldBeUppercase() {
        // Act
        SKU sut = new SKU();

        // Assert
        assertEquals(sut.getValue(), sut.getValue().toUpperCase());
    }

    @Test
    void skuShouldBeEqualToItself() {
        // Arrange
        SKU sut = new SKU();

        // Act
        boolean result = sut.equals(sut);

        // Assert
        assertTrue(result);
    }

    @Test
    void skuShouldNotBeEqualToNull() {
        // Arrange
        SKU sut = new SKU();

        // Act
        boolean result = sut.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void skuShouldNotBeEqualToDifferentType() {
        // Arrange
        SKU sut = new SKU();

        // Act
        boolean result = sut.equals("NOT_A_SKU");

        // Assert
        assertFalse(result);
    }

    @Test
    void differentGeneratedSkusShouldNormallyNotBeEqual() {
        // Arrange
        SKU sut = new SKU();
        SKU otherSku = new SKU();

        // Act
        boolean result = sut.equals(otherSku);

        // Assert
        assertFalse(result);
    }

    @Test
    void toStringShouldReturnSkuValue() {
        // Arrange
        SKU sut = new SKU();

        // Act
        String result = sut.toString();

        // Assert
        assertEquals(sut.getValue(), result);
    }

    @Test
    void hashCodeShouldBeStableForSameObject() {
        // Arrange
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
}