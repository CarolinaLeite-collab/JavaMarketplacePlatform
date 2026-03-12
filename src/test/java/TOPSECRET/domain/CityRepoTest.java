package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import java.util.List;
import static org.mockito.Mockito.*;


class CityRepoTest {

    private CityFactory _cityFactoryDouble;
    private CityRepo _cityRepoDouble;
    private City _city1Double;
    private City _city2Double;

    private Country _countryDouble;
    private Country _otherCountryDouble;


    @Test
    void constructorWithFactoryDoesNotThrow() {
        _cityFactoryDouble = mock(CityFactory.class);
        assertDoesNotThrow(() -> new CityRepo(_cityFactoryDouble));
    }

    @Test
    void addCallsFactoryAndStoresReturnedCity() {
        // Arrange
        _cityFactoryDouble = mock(CityFactory.class);
        _cityRepoDouble = new CityRepo(_cityFactoryDouble); // SUT
        _city1Double = mock(City.class);
        _countryDouble = mock(Country.class);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_city1Double);
        when(_city1Double.getName()).thenReturn("Porto");
        when(_city1Double.getCountry()).thenReturn(_countryDouble);

        // Act
        City created = _cityRepoDouble.add("Porto", _countryDouble);

        // Assert
        assertSame(_city1Double, created);
        assertTrue(_cityRepoDouble.existsByNameAndCountry("Porto", _countryDouble));
    }

    @Test
    void addDuplicateCityThrowsIllegalStateException() {
        // Arrange
        _cityFactoryDouble = mock(CityFactory.class);
        _cityRepoDouble = new CityRepo(_cityFactoryDouble);  // SUT
        _city1Double = mock(City.class);
        _countryDouble = mock(Country.class);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_city1Double);
        when(_city1Double.getName()).thenReturn("Porto");
        when(_city1Double.getCountry()).thenReturn(_countryDouble);

        _cityRepoDouble.add("Porto", _countryDouble);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _cityRepoDouble.add("Porto", _countryDouble)
        );

        assertEquals("City already exists for this country", exception.getMessage());
    }

    @Test
    void existsByNameAndCountryIsCaseInsensitiveAndTrims() {
        // Arrange
        _cityFactoryDouble = mock(CityFactory.class);
        _cityRepoDouble = new CityRepo(_cityFactoryDouble);  // SUT
        _city1Double = mock(City.class);
        _countryDouble = mock(Country.class);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_city1Double);
        when(_city1Double.getName()).thenReturn("Porto");
        when(_city1Double.getCountry()).thenReturn(_countryDouble);

        _cityRepoDouble.add("Porto", _countryDouble);

        // Act & Assert
        assertTrue(_cityRepoDouble.existsByNameAndCountry(" porto ", _countryDouble));
    }

    @Test
    void existsByNameAndCountryReturnsFalseWhenNotFound() {
        _cityFactoryDouble = mock(CityFactory.class);
        _cityRepoDouble = new CityRepo(_cityFactoryDouble);  // SUT
        _countryDouble = mock(Country.class);

        // Act & Assert
        assertFalse(_cityRepoDouble.existsByNameAndCountry("Braga", _countryDouble));
    }

    @Test
    void existsByNameAndCountryReturnsFalseForDifferentCountry() {
        // Arrange
        _cityFactoryDouble = mock(CityFactory.class);
        _cityRepoDouble = new CityRepo(_cityFactoryDouble);  // SUT
        _city1Double = mock(City.class);
        _countryDouble = mock(Country.class);
        _otherCountryDouble = mock(Country.class);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_city1Double);
        when(_city1Double.getName()).thenReturn("Porto");
        when(_city1Double.getCountry()).thenReturn(_countryDouble);

        _cityRepoDouble.add("Porto", _countryDouble);

        // Act & Assert
        assertFalse(_cityRepoDouble.existsByNameAndCountry("Porto", _otherCountryDouble));
    }

    @Test
    void existsByNameAndCountryNullArgumentsReturnFalse() {
        // Arrange
        _cityFactoryDouble = mock(CityFactory.class);
        _cityRepoDouble = new CityRepo(_cityFactoryDouble);  // SUT
        _countryDouble = mock(Country.class);

        // Act & Assert
        assertFalse(_cityRepoDouble.existsByNameAndCountry(null, _countryDouble));
        assertFalse(_cityRepoDouble.existsByNameAndCountry("Porto", null));
        assertFalse(_cityRepoDouble.existsByNameAndCountry(null, null));
    }

    @Test
    void getAllReturnsUnmodifiableList() {
        // Arrange
        _cityFactoryDouble = mock(CityFactory.class);
        _cityRepoDouble = new CityRepo(_cityFactoryDouble);  // SUT
        _countryDouble = mock(Country.class);
        _city1Double = mock(City.class);
        _city2Double = mock(City.class);

        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_city1Double);
        when(_cityFactoryDouble.createCity("Lisbon", _countryDouble)).thenReturn(_city2Double);
        when(_city1Double.getName()).thenReturn("Porto");
        when(_city1Double.getCountry()).thenReturn(_countryDouble);
        when(_city2Double.getName()).thenReturn("Lisbon");
        when(_city2Double.getCountry()).thenReturn(_countryDouble);

        _cityRepoDouble.add("Porto", _countryDouble);
        _cityRepoDouble.add("Lisbon", _countryDouble);

        // Act
        List<City> all = _cityRepoDouble.getAll();

        // Assert
        assertEquals(2, all.size());
        assertTrue(all.contains(_city1Double));
        assertTrue(all.contains(_city2Double));
        assertThrows(UnsupportedOperationException.class, () -> all.add(mock(City.class)));
    }

    @Test
    void getAll_whenEmptyReturnsEmptyUnmodifiableList() {
        // Arrange
        _cityFactoryDouble = mock(CityFactory.class);
        _cityRepoDouble = new CityRepo(_cityFactoryDouble);  // SUT

        // Act
        List<City> all = _cityRepoDouble.getAll();

        // Assert
        assertNotNull(all);
        assertTrue(all.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> all.add(mock(City.class)));
    }
}