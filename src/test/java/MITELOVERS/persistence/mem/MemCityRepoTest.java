package MITELOVERS.persistence.mem;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemCityRepoTest {

    private City _cityDouble;
    private City _cityDouble2;
    private CityId _cityId1;
    private CityId _cityId2;

    @BeforeEach
    void setUp() {
        _cityDouble = mock(City.class);
        _cityDouble2 = mock(City.class);
        _cityId1 = new CityId("Porto", new CountryId("PT"));
        _cityId2 = new CityId("Lisboa", new CountryId("PT"));

        when(_cityDouble.identity()).thenReturn(_cityId1);
        when(_cityDouble2.identity()).thenReturn(_cityId2);
    }

    private int count(Iterable<?> iterable) {
        int count = 0;
        for (Object ignored : iterable) count++;
        return count;
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        // SUT & Act & Assert
        assertDoesNotThrow(MemCityRepo::new);
    }

    @Test
    void saveShouldReturnCityForNewCity() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act
        City result = repo.save(_cityDouble);

        // Assert
        assertSame(_cityDouble, result);
    }

    @Test
    void saveShouldAddCityToRepo() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act
        repo.save(_cityDouble);

        // Assert
        assertEquals(1, count(repo.findAll()));
    }

    @Test
    void saveShouldAllowMultipleDistinctCities() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act
        repo.save(_cityDouble);
        repo.save(_cityDouble2);

        // Assert
        assertEquals(2, count(repo.findAll()));
    }

    @Test
    void containsOfIdentityShouldReturnTrueIfCityExists() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act
        repo.save(_cityDouble);

        // Assert
        assertTrue(repo.containsOfIdentity(_cityId1));
    }

    @Test
    void containsOfIdentityShouldReturnFalseIfCityDoesNotExist() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act & Assert
        assertFalse(repo.containsOfIdentity(_cityId1));
    }

    @Test
    void findAllShouldReturnUnmodifiableCollection() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act
        repo.save(_cityDouble);
        Iterable<City> result = repo.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> ((java.util.Collection<City>) result).add(_cityDouble2));
    }

    @Test
    void findAllShouldReturnEmptyWhenNoCity() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act & Assert
        assertEquals(0, count(repo.findAll()));
    }

    @Test
    void ofIdentityShouldReturnCityIfExists() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act
        repo.save(_cityDouble);
        Optional<City> result = repo.ofIdentity(_cityId1);

        // Assert
        assertTrue(result.isPresent());
        assertSame(_cityDouble, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyIfCityDoesNotExist() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act
        Optional<City> result = repo.ofIdentity(_cityId1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllKeysShouldReturnEmptyWhenNoCity() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act & Assert
        assertTrue(repo.findAllKeys().isEmpty());
    }

    @Test
    void findAllKeysShouldReturnAllKeys() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act
        repo.save(_cityDouble);
        repo.save(_cityDouble2);
        List<CityId> keys = repo.findAllKeys();

        // Assert
        assertEquals(2, keys.size());
        assertTrue(keys.contains(_cityId1));
        assertTrue(keys.contains(_cityId2));
    }

    @Test
    void findAllKeysShouldReturnMutableList() {
        // SUT
        MemCityRepo repo = new MemCityRepo();

        // Act
        repo.save(_cityDouble);
        List<CityId> keys = repo.findAllKeys();

        // Assert
        assertDoesNotThrow(() -> keys.add(_cityId2));
    }
}