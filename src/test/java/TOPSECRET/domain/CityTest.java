package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void constructor_validArgumentsCreatesCity() {
        // Arrange
        Country country = new Country("Portugal");

        // Act
        City city = new City("Porto", country);

        // Assert
        assertEquals("Porto", city.getName());
        assertEquals(country, city.getCountry());
    }

    @Test
    void constructor_nullOrBlankNameThrows() {
        // Arrange
        Country country = new Country("Portugal");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new City(null, country));
        assertThrows(IllegalArgumentException.class, () -> new City("   ", country));
    }

    @Test
    void constructor_nullCountryThrows() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new City("Name", null));
    }

    @Test
    void equalsAndHashCode() {
        // Arrange
        Country country = new Country("Portugal");
        City a = new City("Lisbon", country);
        City b = new City("Lisbon", country);
        City c = new City("Porto", country);

        // Assert
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void constructor_trimsName() {
        // Arrange
        Country country = new Country("Portugal");

        // Act
        City city = new City("  Porto  ", country);

        // Assert
        assertEquals("Porto", city.getName());
    }

    @Test
    void hashCode_differsForDifferentCities() {
        // Arrange
        Country country = new Country("Portugal");
        City porto = new City("Porto", country);
        City lisbon = new City("Lisbon", country);

        // Assert
        assertNotEquals(porto.hashCode(), lisbon.hashCode());
    }

    @Test
    void toString_formatsCorrectly() {
        // Arrange
        Country country = new Country("Portugal");
        City city = new City("Porto", country);

        // Act
        String expected = String.format("%s, %s", city.getName(), country.getCountryName());

        // Assert
        assertEquals(expected, city.toString());
    }

    @Test
    void equals_returnsFalseWhenComparingWithDifferentTypeAndNull() {
        // Arrange
        City city = new City("Porto", new Country("Portugal"));

        // Assert
        assertFalse(city.equals(new Object()));
        assertFalse(city.equals(null));
    }

    @Test
    void equals_sameReference_returnsTrue() {
        // Arrange
        City city = new City("Porto", new Country("Portugal"));

        // Assert
        assertSame(city, city);
    }

    @Test
    void equals_isCaseInsensitiveForName() {
        // Arrange
        Country country = new Country("Portugal");
        City a = new City("porto", country);
        City b = new City("PORTO", country);

        // Assert
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_sameNameDifferentCountry_returnsFalse() {
        // Arrange
        City a = new City("Porto", new Country("Portugal"));
        City b = new City("Porto", new Country("Spain"));

        // Assert
        assertNotEquals(a, b);
    }
}
