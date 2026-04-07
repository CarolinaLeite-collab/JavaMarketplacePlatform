package TOPSECRET.persistence.mem;

import TOPSECRET.domain.city.City;
import TOPSECRET.domain.valueobject.CityId;
import TOPSECRET.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        assertEquals(1, ((java.util.List<City>) repo.findAll()).size());
    }

    @Test
    void saveShouldThrowForDuplicateCity() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.save(_cityDouble);

        assertThrows(IllegalStateException.class, () -> repo.save(_cityDouble));
    }

    @Test
    void saveShouldNotAddDuplicateCity() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.save(_cityDouble);

        assertThrows(IllegalStateException.class, () -> repo.save(_cityDouble));

        assertEquals(1, ((java.util.List<City>) repo.findAll()).size());
    }

    @Test
    void saveShouldAllowMultipleDistinctCities() {
        MemoCityRepo repo = new MemoCityRepo();

        repo.save(_cityDouble);
        repo.save(_cityDouble2);

        assertEquals(2, ((java.util.List<City>) repo.findAll()).size());
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
    void findAllShouldReturnImmutableList() {
        MemoCityRepo repo = new MemoCityRepo();
        repo.save(_cityDouble);

        java.util.List<City> result = (java.util.List<City>) repo.findAll();

        assertThrows(UnsupportedOperationException.class, () -> result.add(_cityDouble2));
    }

    @Test
    void findAllShouldReturnEmptyWhenNoCity() {
        MemoCityRepo repo = new MemoCityRepo();

        java.util.List<City> result = (java.util.List<City>) repo.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
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
}