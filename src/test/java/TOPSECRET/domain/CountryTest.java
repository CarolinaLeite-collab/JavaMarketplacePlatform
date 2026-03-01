package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    @Test
    void testConstructor() {
        new Country("Lebanon");
    }

    @Test
    void constructsCountrySuccessfully() {
        // Act
        Country country = new Country("France");

        // Assert
        assertNotNull(country);
        assertEquals("France", country.getCountryName());
    }

    @Test
    void throwsIfCountryNameNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Country(null));
    }

    @Test
    void throwsIfCountryNameEmpty() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Country("   "));
    }

    @Test
    void returnsTrueIfSameCountry() {
        // Arrange
        Country c1 = new Country("France");
        Country c2 = new Country("France");

        // Act
        boolean result = c1.equals(c2);

        // Assert
        assertTrue(result);
    }

    @Test
    void returnsFalseIfDifferentCountry() {
        // Arrange
        Country c1 = new Country("France");
        Country c2 = new Country("Germany");

        // Act
        boolean result = c1.equals(c2);

        // Assert
        assertFalse(result);
    }

    @Test
    void returnsFalseForNonCountry() {
        // Arrange
        Country c = new Country("France");
        Object notACountry = "Not a Country";

        // Act
        boolean result = c.equals(notACountry);

        // Assert
        assertFalse(result);
    }

    @Test
    void returnsFalseIfCountryNull() {
        // Arrange
        Country c = new Country("France");

        // Act
        boolean result = c.equals(null);

        // Assert
        assertFalse(result);
    }

}