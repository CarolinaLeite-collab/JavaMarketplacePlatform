package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void constructor_validArgumentsCreatesCity() {
        Country country = new Country("Portugal", new User(new Name("Admin"), new Address("a","1", Address.BuildingType.HOUSE,"city","region", Address.Country.PORTUGAL, "1000-000", null), new Email("a@b.c"), new Phone(new PhonePrefix("+351"), "912345678")), LocalDate.of(2020,1,1));
        City city = new City("Porto", country, LocalDate.now());
        assertEquals("Porto", city.getName());
        assertEquals(country, city.getCountry());
        assertNotNull(city.getCreatedDate());
    }

    @Test
    void constructor_nullOrBlankNameThrows() {
        Country country = new Country("Portugal", new User(new Name("Admin"), new Address("a","1", Address.BuildingType.HOUSE,"city","region", Address.Country.PORTUGAL, "1000-000", null), new Email("a@b.c"), new Phone(new PhonePrefix("+351"), "912345678")), LocalDate.of(2020,1,1));
        assertThrows(IllegalArgumentException.class, () -> new City(null, country, LocalDate.now()));
        assertThrows(IllegalArgumentException.class, () -> new City("   ", country, LocalDate.now()));
    }

    @Test
    void constructor_nullCountryThrows() {
        assertThrows(IllegalArgumentException.class, () -> new City("Name", null, LocalDate.now()));
    }

    @Test
    void equalsAndHashCode() {
        Country country = new Country("Portugal", new User(new Name("Admin"), new Address("a","1", Address.BuildingType.HOUSE,"city","region", Address.Country.PORTUGAL, "1000-000", null), new Email("a@b.c"), new Phone(new PhonePrefix("+351"), "912345678")), LocalDate.of(2020,1,1));
        City a = new City("Lisbon", country, LocalDate.now());
        City b = new City("Lisbon", country, LocalDate.now());
        City c = new City("Porto", country, LocalDate.now());

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
