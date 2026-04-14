package TOPSECRET.persistence.mem;

import TOPSECRET.domain.city.City;
import TOPSECRET.domain.valueobject.CityId;
import TOPSECRET.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoCityRepoTest {

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

    private <T> long count(Iterable<T> iterable) {
        long count = 0;
        for (T ignored : iterable) count++;
        return count;
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        MemoCityRepo repo = new MemoCityRepo();

        assertNotNull(repo);
    }

    @Test
    void saveShouldReturnCityForNewCity() {
        MemoCityRepo repo = new MemoCityRepo();

        City result = repo.save(_cityDouble);

        assertSame(_cityDouble, result);
    }

    @Test
    void saveShouldAddCityToRepo() {
        MemoCityRepo repo = new MemoCityRepo();

        repo.save(_cityDouble);

        assertEquals(1, count(repo.findAll()));
    }

    @Test
    void saveShouldAllowMultipleDistinctCities() {
        MemoCityRepo repo = new MemoCityRepo();

        repo.save(_cityDouble);
        repo.save(_cityDouble2);

        assertEquals(2, count(repo.findAll()));
    }

    @Test
    void addCityShouldReturnCityForNewCity() {
        MemoCityRepo repo = new MemoCityRepo();

        City result = repo.addCity(_cityDouble);

        assertSame(_cityDouble, result);
    }

    @Test
    void addCityShouldThrowForDuplicateCity() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.addCity(_cityDouble);

        assertThrows(IllegalStateException.class, () -> repo.addCity(_cityDouble));
    }

    @Test
    void addCityShouldNotAddDuplicateCity() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.addCity(_cityDouble);

        assertThrows(IllegalStateException.class, () -> repo.addCity(_cityDouble));

        assertEquals(1, count(repo.findAll()));
    }

    @Test
    void addCityShouldThrowCorrectMessage() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.addCity(_cityDouble);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> repo.addCity(_cityDouble));

        assertEquals("City already exists for this country", ex.getMessage());
    }

    @Test
    void containsOfIdentityShouldReturnTrueIfCityExists() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.save(_cityDouble);

        assertTrue(repo.containsOfIdentity(_cityId1));
    }

    @Test
    void containsOfIdentityShouldReturnFalseIfCityDoesNotExist() {
        MemoCityRepo repo = new MemoCityRepo();

        assertFalse(repo.containsOfIdentity(_cityId1));
    }

    @Test
    void findAllShouldReturnUnmodifiableCollection() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.save(_cityDouble);

        Iterable<City> result = repo.findAll();

        assertThrows(UnsupportedOperationException.class,
                () -> ((java.util.Collection<City>) result).add(_cityDouble2));
    }

    @Test
    void findAllShouldReturnEmptyWhenNoCity() {
        MemoCityRepo repo = new MemoCityRepo();

        assertEquals(0, count(repo.findAll()));
    }

    @Test
    void ofIdentityShouldReturnCityIfExists() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.save(_cityDouble);

        Optional<City> result = repo.ofIdentity(_cityId1);

        assertTrue(result.isPresent());
        assertSame(_cityDouble, result.get());
    }

    @Test
    void ofIdentityShouldReturnEmptyIfCityDoesNotExist() {
        MemoCityRepo repo = new MemoCityRepo();

        Optional<City> result = repo.ofIdentity(_cityId1);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllKeysShouldReturnEmptyWhenNoCity() {
        MemoCityRepo repo = new MemoCityRepo();

        assertTrue(repo.findAllKeys().isEmpty());
    }

    @Test
    void findAllKeysShouldReturnAllKeys() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.save(_cityDouble);
        repo.save(_cityDouble2);

        List<CityId> keys = repo.findAllKeys();

        assertEquals(2, keys.size());
        assertTrue(keys.contains(_cityId1));
        assertTrue(keys.contains(_cityId2));
    }

    @Test
    void findAllKeysShouldReturnMutableList() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.save(_cityDouble);

        List<CityId> keys = repo.findAllKeys();

        assertDoesNotThrow(() -> keys.add(_cityId2));
    }

}