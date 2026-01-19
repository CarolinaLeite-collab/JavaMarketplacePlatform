package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CityRepoTest {

    private CityRepo repo;
    private Country country;

    @BeforeEach
    void setUp() {
        repo = new CityRepo();
        country = new Country("Portugal", new User(new Name("Admin"), new Address("a","1", Address.BuildingType.HOUSE,"city","region", Address.Country.PORTUGAL, "1000-000", null), new Email("a@b.c"), new Phone(new PhonePrefix("+351"), "912345678")), LocalDate.of(2020,1,1));
    }

    @Test
    void save_and_exists() {
        City city = new City("Porto", country, LocalDate.now());
        assertFalse(repo.existsByNameAndCountry("Porto", country));
        City saved = repo.save(city);
        assertNotNull(saved);
        assertTrue(repo.existsByNameAndCountry("Porto", country));
        // duplicate save returns null
        assertNull(repo.save(new City("Porto", country, LocalDate.now())));
    }

    @Test
    void save_nullThrows() {
        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
    }
}
