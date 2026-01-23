package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCountryControllerTest {

    private CountryRepo _countryRepo;
    private RegisterCountryController _controller;
    private User _admin;

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
        _admin = new User(new Name("Marcelo"), address, new Email("test@test.pt"), phone);

        _countryRepo = new CountryRepo();
        _controller = new RegisterCountryController(_countryRepo, _admin);
    }

    @Test
    void constructsControllerSuccessfully() {
        // Assert
        assertNotNull(_controller);
    }

    @Test
    void shouldRegisterCountrySuccessfully() {
        // Act
        Country country = _controller.registerCountry("Portugal");

        // Assert
        assertNotNull(country);
        assertEquals(1, _countryRepo.getAllCountries().size());
        assertEquals(country, _countryRepo.getAllCountries().get(0));
    }

    @Test
    void shouldNotRegisterCountrySuccessfully() {
        //Arrange
        Country country = _countryRepo.registerCountry("Portugal");
        List<Country> countries = _countryRepo.getAllCountries();

        // Act
        Country duplicate = _controller.registerCountry("Portugal");

        // Assert
        assertNull(duplicate);
        assertEquals(1, countries.size());
    }
}