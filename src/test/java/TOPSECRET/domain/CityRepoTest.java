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

    @Test
    void existsByNameAndCountry_isCaseInsensitiveAndTrims() {
        repo.save(new City("Porto", country));
        assertTrue(repo.existsByNameAndCountry(" porto ", country));
    }

    @Test
    void getAll_returnsUnmodifiableCopy() {
        City porto = repo.save(new City("Porto", country));
        City lisbon = repo.save(new City("Lisbon", country));
        var all = repo.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(porto));
        assertTrue(all.contains(lisbon));
        assertThrows(UnsupportedOperationException.class, () -> all.add(new City("Braga", country)));
    }
}
