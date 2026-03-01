package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCityControllerTest {

    private CityRepo cityRepo;
    private CountryRepo countryRepo;
    private RegisterCityController controller;
    private Country portugal;

    @BeforeEach
    void setUp() throws InstantiationException {
        cityRepo = new CityRepo();
        countryRepo = new CountryRepo();
        portugal = countryRepo.registerCountry("Portugal");
        controller = new RegisterCityController(cityRepo, countryRepo);
    }

    @Test
    void registerCity_happyPath() {
        // Act
        City c = controller.registerCity("Porto", portugal);

        // Assert
        assertNotNull(c);
        assertEquals("Porto", c.getName());
        assertEquals(portugal, c.getCountry());
    }

    @Test
    void registerCity_duplicateThrows() {
        // Arrange
        controller.registerCity("Porto", portugal);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> controller.registerCity("Porto", portugal));
    }

    @Test
    void registerCity_nullCountryThrows() {
        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("Porto", null));
        assertEquals("Country cannot be null", error.getMessage());
    }

    @Test
    void registerCity_blankOrNullCityNameThrows() {
        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("", portugal));
        assertEquals("City name cannot be null or blank", error.getMessage());
    }

    @Test
    void registerCity_trimmedDuplicateNameThrows() {
        // Arrange
        controller.registerCity("Porto", portugal);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> controller.registerCity(" Porto ", portugal));
    }

    // Tests that getCountries() returns the actual list from the repo and not an empty list.
    @Test void getCountries_returnsAllCountriesFromRepo() throws InstantiationException {
        // Arrange
        countryRepo.registerCountry("Spain");

        // Act
        List<Country> result = controller.getCountries();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getCountryName().equals("Portugal")));
        assertTrue(result.stream().anyMatch(c -> c.getCountryName().equals("Spain"))); }
}
