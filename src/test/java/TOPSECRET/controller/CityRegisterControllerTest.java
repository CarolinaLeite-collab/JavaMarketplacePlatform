package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CityRegisterControllerTest {

    private CityRepo cityRepo;
    private CountryRepo countryRepo;
    private CityRegisterController controller;
    private Country portugal;

    @BeforeEach
    void setUp() {
        cityRepo = new CityRepo();
        countryRepo = new CountryRepo();
        // create a country through CountryRepo
        User admin = new User(new Name("Admin"), new Address("a","1", Address.BuildingType.HOUSE,"city","region", Address.Country.PORTUGAL, "1000-000", null), new Email("a@b.c"), new Phone(new PhonePrefix("+351"), "912345678"));
        portugal = countryRepo.registerCountry("Portugal", admin, LocalDate.of(2020,1,1));
        controller = new CityRegisterController(cityRepo, countryRepo);
    }

    @Test
    void registerCity_happyPath() {
        City c = controller.registerCity("Porto", "Portugal");
        assertNotNull(c);
        assertEquals("Porto", c.getName());
        assertEquals(portugal, c.getCountry());
    }

    @Test
    void registerCity_unknownCountryThrows() {
        assertThrows(IllegalArgumentException.class, () -> controller.registerCity("X", "Atlantis"));
    }

    @Test
    void registerCity_duplicateThrows() {
        controller.registerCity("Porto", "Portugal");
        assertThrows(IllegalStateException.class, () -> controller.registerCity("Porto", "Portugal"));
    }

    @Test
    void registerCity_missingCountryIdThrows() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("Porto", ""));
        assertEquals("Country ID cannot be null or blank", error.getMessage());
    }

    @Test
    void registerCity_blankOrNullCityNameThrows() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("", "Portugal"));
        assertEquals("City name cannot be null or blank", error.getMessage());
    }

    @Test
    void registerCity_trimmedDuplicateNameThrows() {
        controller.registerCity("Porto", "Portugal");
        assertThrows(IllegalStateException.class, () -> controller.registerCity(" Porto ", "Portugal"));
    }
}
