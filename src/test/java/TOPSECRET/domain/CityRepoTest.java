package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import java.util.List;
import static org.mockito.Mockito.*;


class CityRepoTest {

    private CityFactory _cityFactory;
    private CityRepo _cityRepo;

    private City _city1;
    private City _city2;

    private Country _country;
    private Country _otherCountry;


    @Test
    void constructorWithFactoryDoesNotThrow() {
        _cityFactory = mock(CityFactory.class);
        assertDoesNotThrow(() -> new CityRepo(_cityFactory));
    }

    @Test
    void constructorWithNullFactoryThrowsNullPointerException() {
        // Act & Assert
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new CityRepo(null)
        );

        assertEquals("CityFactory cannot be null", exception.getMessage());
    }

    @Test
    void addCallsFactoryAndStoresReturnedCity() {
        // Arrange
        _cityFactory = mock(CityFactory.class);
        _cityRepo = new CityRepo(_cityFactory); // SUT
        _city1 = mock(City.class);
        _country = mock(Country.class);

        when(_cityFactory.createCity("Porto", _country)).thenReturn(_city1);
        when(_city1.getName()).thenReturn("Porto");
        when(_city1.getCountry()).thenReturn(_country);

        // Act
        City created = _cityRepo.add("Porto", _country);

        // Assert
        assertSame(_city1, created);
        assertTrue(_cityRepo.existsByNameAndCountry("Porto", _country));
    }

    @Test
    void addDuplicateCityThrowsIllegalStateException() {
        // Arrange
        _cityFactory = mock(CityFactory.class);
        _cityRepo = new CityRepo(_cityFactory);  // SUT
        _city1 = mock(City.class);
        _country = mock(Country.class);

        when(_cityFactory.createCity("Porto", _country)).thenReturn(_city1);
        when(_city1.getName()).thenReturn("Porto");
        when(_city1.getCountry()).thenReturn(_country);

        _cityRepo.add("Porto", _country);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _cityRepo.add("Porto", _country)
        );

        assertEquals("City already exists for this country", exception.getMessage());
    }

    @Test
    void existsByNameAndCountryIsCaseInsensitiveAndTrims() {
        // Arrange
        _cityFactory = mock(CityFactory.class);
        _cityRepo = new CityRepo(_cityFactory);  // SUT
        _city1 = mock(City.class);
        _country = mock(Country.class);

        when(_cityFactory.createCity("Porto", _country)).thenReturn(_city1);
        when(_city1.getName()).thenReturn("Porto");
        when(_city1.getCountry()).thenReturn(_country);

        _cityRepo.add("Porto", _country);

        // Act & Assert
        assertTrue(_cityRepo.existsByNameAndCountry(" porto ", _country));
    }

    @Test
    void existsByNameAndCountryReturnsFalseWhenNotFound() {
        _cityFactory = mock(CityFactory.class);
        _cityRepo = new CityRepo(_cityFactory);  // SUT
        _country = mock(Country.class);

        // Act & Assert
        assertFalse(_cityRepo.existsByNameAndCountry("Braga", _country));
    }

    @Test
    void existsByNameAndCountryReturnsFalseForDifferentCountry() {
        // Arrange
        _cityFactory = mock(CityFactory.class);
        _cityRepo = new CityRepo(_cityFactory);  // SUT
        _city1 = mock(City.class);
        _country = mock(Country.class);
        _otherCountry = mock(Country.class);

        when(_cityFactory.createCity("Porto", _country)).thenReturn(_city1);
        when(_city1.getName()).thenReturn("Porto");
        when(_city1.getCountry()).thenReturn(_country);

        _cityRepo.add("Porto", _country);

        // Act & Assert
        assertFalse(_cityRepo.existsByNameAndCountry("Porto", _otherCountry));
    }

    @Test
    void existsByNameAndCountryNullArgumentsReturnFalse() {
        // Arrange
        _cityFactory = mock(CityFactory.class);
        _cityRepo = new CityRepo(_cityFactory);  // SUT
        _country = mock(Country.class);

        // Act & Assert
        assertFalse(_cityRepo.existsByNameAndCountry(null, _country));
        assertFalse(_cityRepo.existsByNameAndCountry("Porto", null));
        assertFalse(_cityRepo.existsByNameAndCountry(null, null));
    }

    @Test
    void getAllReturnsUnmodifiableList() {
        // Arrange
        _cityFactory = mock(CityFactory.class);
        _cityRepo = new CityRepo(_cityFactory);  // SUT
        _country = mock(Country.class);
        _city1 = mock(City.class);
        _city2 = mock(City.class);

        when(_cityFactory.createCity("Porto", _country)).thenReturn(_city1);
        when(_cityFactory.createCity("Lisbon", _country)).thenReturn(_city2);
        when(_city1.getName()).thenReturn("Porto");
        when(_city1.getCountry()).thenReturn(_country);
        when(_city2.getName()).thenReturn("Lisbon");
        when(_city2.getCountry()).thenReturn(_country);

        _cityRepo.add("Porto", _country);
        _cityRepo.add("Lisbon", _country);

        // Act
        List<City> all = _cityRepo.getAll();

        // Assert
        assertEquals(2, all.size());
        assertTrue(all.contains(_city1));
        assertTrue(all.contains(_city2));
        assertThrows(UnsupportedOperationException.class, () -> all.add(mock(City.class)));
    }

    @Test
    void getAll_whenEmptyReturnsEmptyUnmodifiableList() {
        // Arrange
        _cityFactory = mock(CityFactory.class);
        _cityRepo = new CityRepo(_cityFactory);  // SUT

        // Act
        List<City> all = _cityRepo.getAll();

        // Assert
        assertNotNull(all);
        assertTrue(all.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> all.add(mock(City.class)));
    }
}