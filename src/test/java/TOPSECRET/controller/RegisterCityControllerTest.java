package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCityControllerTest {

    private CityRepo cityRepo;
    private CountryRepo countryRepo;
    private RegisterCityController controller;
    private Country portugal;

    @BeforeEach
    void setUp() {
        cityRepo = new CityRepo();
        countryRepo = new CountryRepo();
        portugal = countryRepo.registerCountry("Portugal");
        controller = new RegisterCityController(cityRepo, countryRepo);
    }

    @Test
    void registerCity_happyPath() {
        City c = controller.registerCity("Porto", portugal);
        assertNotNull(c);
        assertEquals("Porto", c.getName());
        assertEquals(portugal, c.getCountry());
    }

    @Test
    void registerCity_duplicateThrows() {
        controller.registerCity("Porto", portugal);
        assertThrows(IllegalStateException.class, () -> controller.registerCity("Porto", portugal));
    }

    @Test
    void registerCity_nullCountryThrows() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("Porto", null));
        assertEquals("Country cannot be null", error.getMessage());
    }

    @Test
    void registerCity_blankOrNullCityNameThrows() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("", portugal));
        assertEquals("City name cannot be null or blank", error.getMessage());
    }

    @Test
    void registerCity_trimmedDuplicateNameThrows() {
        controller.registerCity("Porto", portugal);
        assertThrows(IllegalStateException.class, () -> controller.registerCity(" Porto ", portugal));
    }
}
