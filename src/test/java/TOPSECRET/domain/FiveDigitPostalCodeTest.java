package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FiveDigitPostalCodeTest {

    @Test
    void shouldInstantiateFiveDigitPostalCodeWithValidData() {
        // Arrange
        String postalCode = "12345";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("GERMANY");

        // Act
        FiveDigitPostalCode fiveDigitPostalCode = new FiveDigitPostalCode(country, postalCode);

        // Assert
        assertEquals("12345", fiveDigitPostalCode.getValue());
        assertEquals(country, fiveDigitPostalCode.getPostalCodeCountry());
    }

    @Test
    void shouldNormalizePostalCodeRemovingNonDigits() {
        // Arrange
        String postalCode = "12-345";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("FRANCE");

        // Act
        FiveDigitPostalCode fiveDigitPostalCode = new FiveDigitPostalCode(country, postalCode);

        // Assert
        assertEquals("12345", fiveDigitPostalCode.getValue());
    }

    @Test
    void shouldThrowWhenPostalCodeIsNull() {
        // Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("SPAIN");

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FiveDigitPostalCode(country, null)
        );

        // Assert
        assertEquals("Postal code cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowWhenPostalCodeDoesNotHaveFiveDigits() {
        // Arrange
        String postalCode = "1234";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("ITALY");

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FiveDigitPostalCode(country, postalCode)
        );

        // Assert
        assertEquals("Postal code must contain exactly 5 digits", exception.getMessage());
    }

    @Test
    void shouldThrowWhenCountryIsNull() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FiveDigitPostalCode(null, "12345")
        );

        // Assert
        assertEquals("Country cannot be null", exception.getMessage());
    }
}