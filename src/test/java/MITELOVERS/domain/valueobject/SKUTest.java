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
    void constructorWithValidStringShouldCreateSKUWithGivenValue() {
        // Arrange
        String value = "ABCDEF1234";

        // Act
        SKU sut = new SKU(value);

        // Assert
        assertEquals(value, sut.getValue());
        assertEquals(value, sut.toString());
    }

    @Test
    void constructorWithNullStringShouldThrowIllegalArgumentException() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SKU(null)
        );

        assertEquals("SKU cannot be null or blank.", exception.getMessage());
    }

    @Test
    void constructorWithBlankStringShouldThrowIllegalArgumentException() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SKU("   ")
        );

        assertEquals("SKU cannot be null or blank.", exception.getMessage());
    }

    @Test
    void constructorWithStringNotMatchingFormatShouldThrowIllegalArgumentException() {
        // Arrange
        String invalidValue = "invalid!";

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SKU(invalidValue)
        );

        assertEquals("SKU must match format ^[A-F0-9]{10}$.", exception.getMessage());
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
    void equalsShouldReturnTrueForSKUsWithSameStringValue() {
        // Arrange
        String value = "ABCDEF1234";
        SKU sut = new SKU(value);
        SKU other = new SKU(value);

        // Act
        boolean result = sut.equals(other);

        // Assert
        assertTrue(result);
        assertEquals(sut.hashCode(), other.hashCode());
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

    @Test
    void shouldCreateSkuFromValidValue() {
        //Arrange
        String value = "ABC123DEF0";

        //SUT
        SKU sku = new SKU(value);

        //Act
        boolean result = value.equals(sku.toString());

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        //Arrange
        String value = null;

        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new SKU(value));

        //Assert
        assertEquals("Invalid SKU format", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSKUInvalid(){
        //Arrange
        String value = "23";

        //Act
        //SUT
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new SKU(value));

        //Assert
        assertEquals("Invalid SKU format", exception.getMessage());
    }
}

