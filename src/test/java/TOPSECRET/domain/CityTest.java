package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void constructor_validArgumentsCreatesCity() {
        Country country = new Country("Portugal");
        City city = new City("Porto", country);
        assertEquals("Porto", city.getName());
        assertEquals(country, city.getCountry());
    }

    @Test
    void constructor_nullOrBlankNameThrows() {
        Country country = new Country("Portugal");
        assertThrows(IllegalArgumentException.class, () -> new City(null, country));
        assertThrows(IllegalArgumentException.class, () -> new City("   ", country));
    }

    @Test
    void constructor_nullCountryThrows() {
        assertThrows(IllegalArgumentException.class, () -> new City("Name", null));
    }

    @Test
    void equalsAndHashCode() {
        Country country = new Country("Portugal");
        City a = new City("Lisbon", country);
        City b = new City("Lisbon", country);
        City c = new City("Porto", country);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
