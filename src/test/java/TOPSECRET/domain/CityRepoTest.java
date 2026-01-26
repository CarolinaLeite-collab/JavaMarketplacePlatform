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
        // Arrange
        City city = new City("Porto", country);

        // Act
        assertFalse(repo.existsByNameAndCountry("Porto", country));
        City saved = repo.save(city);

        // Assert
        assertNotNull(saved);
        assertTrue(repo.existsByNameAndCountry("Porto", country));

        // Act & Assert: duplicate saves return null
        assertNull(repo.save(new City("Porto", country)));
    }

    @Test
    void save_nullThrows() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
    }

    @Test
    void existsByNameAndCountry_isCaseInsensitiveAndTrims() {
        // Arrange
        repo.save(new City("Porto", country));

        // Act & Assert
        assertTrue(repo.existsByNameAndCountry(" porto ", country));
    }

    @Test
    void getAll_returnsUnmodifiableCopy() {
        // Arrange
        City porto = repo.save(new City("Porto", country));
        City lisbon = repo.save(new City("Lisbon", country));

        // Act
        var all = repo.getAll();

        // Assert
        assertEquals(2, all.size());
        assertTrue(all.contains(porto));
        assertTrue(all.contains(lisbon));
        assertThrows(UnsupportedOperationException.class, () -> all.add(new City("Braga", country)));
    }

    @Test
    void existsByNameAndCountry_nullArgumentsReturnFalse() {
        // Act & Assert
        assertFalse(repo.existsByNameAndCountry(null, country));
        assertFalse(repo.existsByNameAndCountry("Porto", null));
        assertFalse(repo.existsByNameAndCountry(null, null));
    }

    @Test
    void existsByNameAndCountry_returnsFalseWhenNotFound() {
        // Act & Assert
        assertFalse(repo.existsByNameAndCountry("Braga", country));
    }
}
