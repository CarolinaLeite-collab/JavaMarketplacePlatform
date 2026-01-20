package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCountryControllerTest {

    private CountryRepo countryRepo;
    private RegisterCountryController controller;
    private User admin;

    @BeforeEach
    void setUp() {
        // Arrange
        countryRepo = new CountryRepo();
        controller = new RegisterCountryController(countryRepo);

        Address address = new Address(
                "Rua Dr.Amilcar de Castro", "24",
                Address.BuildingType.HOUSE,
                "Barcelos", "Braga",
                Address.Country.PORTUGAL,
                "4775-105", null
        );

        Phone phone = new Phone(new PhonePrefix("+351"), "962064343");
        admin = new User(new Name("Marcelo"), address, new Email("test@test.pt"), phone);
    }

    @Test
    void registerCountry_shouldCreateAndStoreCountry() {
        // Act
        Country country = controller.registerCountry("Portugal", admin);

        // Assert
        assertNotNull(country);
        assertEquals(1, countryRepo.getAllCountries().size());
        assertEquals(country, countryRepo.getAllCountries().get(0));
    }


}