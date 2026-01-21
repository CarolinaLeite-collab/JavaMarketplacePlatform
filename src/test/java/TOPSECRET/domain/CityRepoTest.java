package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityRepoTest {

    private CityRepo repo;
    private Country country;

    @BeforeEach
    void setUp() {
        repo = new CityRepo();
        country = new Country("Portugal");
    }

    @Test
    void save_and_exists() {
        City city = new City("Porto", country);
        assertFalse(repo.existsByNameAndCountry("Porto", country));
        City saved = repo.save(city);
        assertNotNull(saved);
        assertTrue(repo.existsByNameAndCountry("Porto", country));
        // duplicate save returns null
        assertNull(repo.save(new City("Porto", country)));
    }

    @Test
    void save_nullThrows() {
        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
    }
}
