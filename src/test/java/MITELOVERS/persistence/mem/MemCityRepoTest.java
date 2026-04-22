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


    private <T> long count(Iterable<T> iterable) {
        long count = 0;
        for (T ignored : iterable) count++;
        return count;
    }


    @Test
    void shouldConstructRepoSuccessfully() {
        MemCityRepo repo = new MemCityRepo();

        assertNotNull(repo);
    }


    @Test
    void saveShouldReturnCityForNewCity() {
        MemCityRepo repo = new MemCityRepo();

        City result = repo.save(_cityDouble);

        assertSame(_cityDouble, result);
    }


    @Test
    void saveShouldAddCityToRepo() {
        MemCityRepo repo = new MemCityRepo();

        repo.save(_cityDouble);

        assertEquals(1, count(repo.findAll()));
    }


    @Test
    void saveShouldAllowMultipleDistinctCities() {
        MemCityRepo repo = new MemCityRepo();

        repo.save(_cityDouble);
        repo.save(_cityDouble2);

        assertEquals(2, count(repo.findAll()));
    }


    @Test
    void containsOfIdentityShouldReturnTrueIfCityExists() {
        MemCityRepo repo = new MemCityRepo();
        repo.save(_cityDouble);

        assertTrue(repo.containsOfIdentity(_cityId1));
    }


    @Test
    void containsOfIdentityShouldReturnFalseIfCityDoesNotExist() {
        MemCityRepo repo = new MemCityRepo();

        assertFalse(repo.containsOfIdentity(_cityId1));
    }


    @Test
    void findAllShouldReturnUnmodifiableCollection() {
        MemCityRepo repo = new MemCityRepo();
        repo.save(_cityDouble);

        Iterable<City> result = repo.findAll();

        assertThrows(UnsupportedOperationException.class,
                () -> ((java.util.Collection<City>) result).add(_cityDouble2));
    }


    @Test
    void findAllShouldReturnEmptyWhenNoCity() {
        MemCityRepo repo = new MemCityRepo();

        assertEquals(0, count(repo.findAll()));
    }


    @Test
    void ofIdentityShouldReturnCityIfExists() {
        MemCityRepo repo = new MemCityRepo();
        repo.save(_cityDouble);

        Optional<City> result = repo.ofIdentity(_cityId1);

        assertTrue(result.isPresent());
        assertSame(_cityDouble, result.get());
    }


    @Test
    void ofIdentityShouldReturnEmptyIfCityDoesNotExist() {
        MemCityRepo repo = new MemCityRepo();

        Optional<City> result = repo.ofIdentity(_cityId1);

        assertTrue(result.isEmpty());
    }


    @Test
    void findAllKeysShouldReturnEmptyWhenNoCity() {
        MemCityRepo repo = new MemCityRepo();

        assertTrue(repo.findAllKeys().isEmpty());
    }


    @Test
    void findAllKeysShouldReturnAllKeys() {
        MemCityRepo repo = new MemCityRepo();
        repo.save(_cityDouble);
        repo.save(_cityDouble2);

        List<CityId> keys = repo.findAllKeys();

        assertEquals(2, keys.size());
        assertTrue(keys.contains(_cityId1));
        assertTrue(keys.contains(_cityId2));
    }


    @Test
    void findAllKeysShouldReturnMutableList() {
        MemCityRepo repo = new MemCityRepo();
        repo.save(_cityDouble);

        List<CityId> keys = repo.findAllKeys();

        assertDoesNotThrow(() -> keys.add(_cityId2));
    }

}
