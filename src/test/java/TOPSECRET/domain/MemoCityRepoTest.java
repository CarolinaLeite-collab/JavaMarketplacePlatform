package TOPSECRET.domain;

import TOPSECRET.domain.country.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class MemoCityRepoTest {

    private CityFactory _cityFactoryDouble;
    private City _cityDouble;
    private Country _countryDouble;

    @BeforeEach
    void setUp() {

        _cityFactoryDouble = mock(CityFactory.class);
        _cityDouble = mock(City.class);
        _countryDouble = mock(Country.class);
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        // Act & SUT
        MemoCityRepo memoCityRepo = new MemoCityRepo(_cityFactoryDouble);
    }

    @Test
    void registerCityCallsFactoryAndStoresReturnedCity() {
        // Arrange
        when(_cityDouble.getName()).thenReturn("Porto");
        when(_cityDouble.getCountry()).thenReturn(_countryDouble);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);

        // SUT
        MemoCityRepo memoCityRepo = new MemoCityRepo(_cityFactoryDouble);

        // Act
        City created = memoCityRepo.registerCity("Porto", _countryDouble);

        // Assert
        assertSame(_cityDouble, created);
        assertTrue(memoCityRepo.getAllCities().contains(_cityDouble));

    }

    @Test
    void shouldFailToRegisterDuplicatedCity() {
        // Arrange
        when(_cityDouble.getName()).thenReturn("Porto");
        when(_cityDouble.getCountry()).thenReturn(_countryDouble);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);

        // SUT
        MemoCityRepo memoCityRepo = new MemoCityRepo(_cityFactoryDouble);

        // Act
        City first = memoCityRepo.registerCity("Porto", _countryDouble);
        Executable act = () -> memoCityRepo.registerCity("Porto", _countryDouble);

        // Assert
        assertNotNull(first);
        assertThrows(IllegalStateException.class, act);
        assertEquals(1, memoCityRepo.getAllCities().size());
    }

    @Test
    void existsCityInATrims() {
        // Arrange
        when(_cityDouble.getName()).thenReturn("Porto");
        when(_cityDouble.getCountry()).thenReturn(_countryDouble);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);

        // SUT
        MemoCityRepo memoCityRepo = new MemoCityRepo(_cityFactoryDouble);

        //Act
        City first = memoCityRepo.registerCity("Porto", _countryDouble);
        Executable act = () -> memoCityRepo.registerCity(" porto ", _countryDouble);

        //Assert
        assertThrows(IllegalStateException.class, act);
    }

    @Test
    void existsCityInACountryReturnsFalseWhenNotFound() {
        // Arrange
        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);
        when(_cityDouble.getName()).thenReturn("Porto");
        when(_cityDouble.getCountry()).thenReturn(_countryDouble);

        // SUT
        MemoCityRepo memoCityRepo = new MemoCityRepo(_cityFactoryDouble);

        // Act & Assert
        assertFalse(memoCityRepo.existsCityInACountry("Braga", _countryDouble));
    }

    @Test
    void existsCityInACountryReturnsFalseForDifferentCountry() {
        // Arrange
        Country _otherCountryDouble = mock(Country.class);

        when(_cityDouble.getName()).thenReturn("Porto");
        when(_cityDouble.getCountry()).thenReturn(_countryDouble);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);

        // SUT
        MemoCityRepo memoCityRepo = new MemoCityRepo(_cityFactoryDouble);

        // Act
        memoCityRepo.registerCity("Porto", _countryDouble);

        // Assert
        assertFalse(memoCityRepo.existsCityInACountry("Porto", _otherCountryDouble));
    }

    @Test
    void existsCityInACountryNullArgumentsReturnFalse() {
        // Arrange
        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);
        when(_cityDouble.getName()).thenReturn("Porto");
        when(_cityDouble.getCountry()).thenReturn(_countryDouble);

        //SUT
        MemoCityRepo memoCityRepo = new MemoCityRepo(_cityFactoryDouble);

        // Act & Assert
        assertFalse(memoCityRepo.existsCityInACountry(null, _countryDouble));
        assertFalse(memoCityRepo.existsCityInACountry("Porto", null));
        assertFalse(memoCityRepo.existsCityInACountry(null, null));
    }

    @Test
    void shouldGetAllCitiesCitiesList() {
        // Arrange
        City cityDouble = mock(City.class);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);
        when(_cityDouble.getName()).thenReturn("Porto");
        when(_cityDouble.getCountry()).thenReturn(_countryDouble);

        when(_cityFactoryDouble.createCity("Lisbon", _countryDouble)).thenReturn(cityDouble);
        when(cityDouble.getName()).thenReturn("Lisbon");
        when(cityDouble.getCountry()).thenReturn(_countryDouble);

        //SUT
        MemoCityRepo memoCityRepo = new MemoCityRepo(_cityFactoryDouble);

        memoCityRepo.registerCity("Porto", _countryDouble);
        memoCityRepo.registerCity("Lisbon", _countryDouble);

        // Act
        List<City> all = memoCityRepo.getAllCities();

        // Assert
        assertEquals(2, all.size());
        assertTrue(all.contains(_cityDouble));
        assertTrue(all.contains(cityDouble));
        assertThrows(UnsupportedOperationException.class, () -> all.add(mock(City.class)));
    }

    @Test
    void getAllCitiesWhenEmptyReturnsEmptyList() {
        // Arrange & SUT
        MemoCityRepo memoCityRepo = new MemoCityRepo(_cityFactoryDouble);

        // Act
        List<City> all = memoCityRepo.getAllCities();

        // Assert
        assertNotNull(all);
        assertTrue(all.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> all.add(mock(City.class)));
    }
}