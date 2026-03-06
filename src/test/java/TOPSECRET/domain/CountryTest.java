package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    @Test
    void shouldConstructCountrySuccessfully() {
        //Arrange & act
        Country country = new Country("France");
        //Assert
        assertNotNull(country);
    }

    @Test
    void shouldTrimAndNormalizeCountryName() {
        //Arrange & Act
        Country country = new Country("   Portugal   ");
        //Assert
        assertEquals("PORTUGAL", country.getCountryName());
    }

    @Test
    void shouldThrowIfCountryNameIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Country(null));
    }

    @Test
    void shouldThrowIfCountryNameIsEmpty() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Country("   "));
    }

    @Test
    void shouldThrowIfCountryNameHasIllegalChar() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Country("#portugal"));
    }

    @Test
    void shouldReturnTrueForCountriesWithSameName() {
        // Arrange
        Country c1 = new Country("France");
        Country c2 = new Country(" France ");
        // Act
        boolean result = c1.equals(c2);
        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueForSameCountryReference() {
        // Arrange
        Country c1 = new Country("France");
        // Act
        boolean result = c1.equals(c1);
        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForDifferentCountries() {
        // Arrange
        Country c1 = new Country("France");
        Country c2 = new Country("Germany");
        // Act
        boolean result = c1.equals(c2);
        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        // Arrange
        Country country = new Country("France");
        // Assert
        assertNotEquals(null, country);
    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        Country country = new Country("France");
        // Assert
        assertNotEquals("France", country);
        assertFalse(country.equals("Portugal"));
    }
}