package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityFactoryTest {

    private CityFactory factory;
    private Country country;

    @BeforeEach
    void setUp() {
        factory = new CityFactory();
        country = new Country("Portugal");
    }

    @Test
    void createCityWithValidDataReturnsCity() {
        // Arrange
        String cityName = "Porto";

        // Act
        City city = factory.createCity(cityName, country);

        // Assert
        assertNotNull(city);
        assertEquals("Porto", city.getName());
        assertEquals(country, city.getCountry());
    }

    @Test
    void createCityReturnsNewInstanceEachTime() {
        // Act
        City city1 = factory.createCity("Porto", country);
        City city2 = factory.createCity("Porto", country);

        // Assert
        assertNotSame(city1, city2);
        assertEquals(city1, city2);
    }

    @Test
    void createCityWithNullNameThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.createCity(null, country));
    }

    @Test
    void createCityWithBlankNameThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.createCity("   ", country));
    }

    @Test
    void createCityWithNullCountryThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.createCity("Porto", null));
    }
}