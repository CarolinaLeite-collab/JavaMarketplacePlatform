package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import TOPSECRET.domain.City;
import TOPSECRET.domain.CityFactory;
import TOPSECRET.domain.CityRepo;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityRepoTest {

    private CityFactory factory;
    private CityRepo repo;

    private City city1;
    private City city2;

    private Country country;
    private Country otherCountry;

    @BeforeEach
    void setUp() {
        factory = mock(CityFactory.class);
        repo = new CityRepo(factory);

        city1 = mock(City.class);
        city2 = mock(City.class);

        country = mock(Country.class);
        otherCountry = mock(Country.class);

        when(city1.getName()).thenReturn("Porto");
        when(city1.getCountry()).thenReturn(country);

        when(city2.getName()).thenReturn("Lisbon");
        when(city2.getCountry()).thenReturn(country);
    }

    @Test
    void constructorWithFactoryDoesNotThrow() {
        assertDoesNotThrow(() -> new CityRepo(factory));
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
        when(factory.createCity("Porto", country)).thenReturn(city1);

        // Act
        City created = repo.add("Porto", country);

        // Assert
        assertSame(city1, created);
        verify(factory, times(1)).createCity("Porto", country);
        assertTrue(repo.existsByNameAndCountry("Porto", country));
    }

    @Test
    void addDuplicateCityThrowsIllegalStateException() {
        // Arrange
        when(factory.createCity("Porto", country)).thenReturn(city1);
        repo.add("Porto", country);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> repo.add("Porto", country)
        );

        assertEquals("City already exists for this country", exception.getMessage());
        verify(factory, times(1)).createCity("Porto", country);
    }

    @Test
    void existsByNameAndCountryIsCaseInsensitiveAndTrims() {
        // Arrange
        when(factory.createCity("Porto", country)).thenReturn(city1);
        repo.add("Porto", country);

        // Act
        boolean exists = repo.existsByNameAndCountry(" porto ", country);

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByNameAndCountryReturnsFalseWhenNotFound() {
        // Act
        boolean exists = repo.existsByNameAndCountry("Braga", country);

        // Assert
        assertFalse(exists);
    }

    @Test
    void existsByNameAndCountryReturnsFalseForDifferentCountry() {
        // Arrange
        when(factory.createCity("Porto", country)).thenReturn(city1);
        repo.add("Porto", country);

        // Act
        boolean exists = repo.existsByNameAndCountry("Porto", otherCountry);

        // Assert
        assertFalse(exists);
    }

    @Test
    void existsByNameAndCountryNullArgumentsReturnFalse() {
        // Act & Assert
        assertFalse(repo.existsByNameAndCountry(null, country));
        assertFalse(repo.existsByNameAndCountry("Porto", null));
        assertFalse(repo.existsByNameAndCountry(null, null));
    }

    @Test
    void getAllReturnsUnmodifiableList() {
        // Arrange
        when(factory.createCity("Porto", country)).thenReturn(city1);
        when(factory.createCity("Lisbon", country)).thenReturn(city2);

        repo.add("Porto", country);
        repo.add("Lisbon", country);

        // Act
        List<City> all = repo.getAll();

        // Assert
        assertEquals(2, all.size());
        assertTrue(all.contains(city1));
        assertTrue(all.contains(city2));
        assertThrows(UnsupportedOperationException.class, () -> all.add(mock(City.class)));
    }

    @Test
    void getAll_whenEmptyReturnsEmptyUnmodifiableList() {
        // Act
        List<City> all = repo.getAll();

        // Assert
        assertNotNull(all);
        assertTrue(all.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> all.add(mock(City.class)));
    }
}