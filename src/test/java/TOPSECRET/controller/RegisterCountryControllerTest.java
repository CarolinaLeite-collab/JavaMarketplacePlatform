package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCountryControllerTest {

    private CountryRepo countryRepo;
    private RegisterCountryController controller;
    private User admin;

    @BeforeEach
    void setUp() {
        // Arrange
        Address address = new Address(
                "Rua Dr. Rui Falcão", "33",
                Address.BuildingType.HOUSE,
                "Barcelos", "Braga",
                Address.Country.PORTUGAL,
                "4790-105", null
        );

        Phone phone = new Phone(new PhonePrefix("+351"), "909978798");
        admin = new User(new Name("Marcelo"), address, new Email("test@test.pt"), phone);

        countryRepo = new CountryRepo();
        controller = new RegisterCountryController(countryRepo, admin);
    }

    @Test
    void constructsControllerSuccessfully() {
        // Assert
        assertNotNull(controller);
    }

    @Test
    void shouldRegisterCountrySuccessfully() {
        // Act
        Country country = controller.registerCountry("Portugal", admin);

        // Assert
        assertNotNull(country);
        assertEquals(1, countryRepo.getAllCountries().size());
        assertEquals(country, countryRepo.getAllCountries().get(0));
    }

    @Test
    void shouldNotRegisterCountrySuccessfully() {
        Country country = countryRepo.registerCountry("Portugal", admin);
        List<Country> countries = countryRepo.getAllCountries();

        // Act
        Country duplicate = controller.registerCountry("Portugal", admin);

        // Assert
        assertNull(duplicate);
        assertEquals(1, countries.size());
    }
}