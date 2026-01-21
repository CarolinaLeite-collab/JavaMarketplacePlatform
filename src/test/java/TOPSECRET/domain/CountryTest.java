package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    private User admin1;
    private User admin2;

    @BeforeEach
    void setUp() {
        // Arrange
        Phone phone = new Phone(new PhonePrefix("+351"), " 962064343 ");
        Address address = new Address(
                "Rua Dr. Rui Falcão", "33",
                Address.BuildingType.HOUSE,
                "Barcelos", "Braga",
                Address.Country.PORTUGAL,
                "4790-105", null
        );
        admin1 = new User(new Name("Marcelo"), address, new Email("1251995@isep.ipp.pt"), phone);
        admin2 = new User(new Name("Marcio"), address, new Email("1251985@isep.ipp.pt"), phone);
    }

    @Test
    void constructsCountrySuccessfully() {
        // Act
        Country country = new Country("France", admin1);

        // Assert
        assertNotNull(country);
        assertEquals("France", country.getCountryName());
        assertEquals(admin1, country.getAdmin());
    }

    @Test
    void throwsIfCountryNameNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Country(null, admin1));
    }

    @Test
    void throwsIfAdminNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Country("France", null));
    }

    @Test
    void returnsTrueIfSameCountry() {
        // Arrange
        Country c1 = new Country("France", admin1);
        Country c2 = new Country("France", admin2);

        // Act
        boolean result = c1.equals(c2);

        // Assert
        assertTrue(result);
    }

    @Test
    void returnsFalseIfDifferentCountry() {
        // Arrange
        Country c1 = new Country("France", admin1);
        Country c2 = new Country("Germany", admin2);

        // Act
        boolean result = c1.equals(c2);

        // Assert
        assertFalse(result);
    }

    @Test
    void returnsFalseForNonCountry() {
        // Arrange
        Country c = new Country("France", admin1);
        Object notACountry = "Not a Country";

        // Act
        boolean result = c.equals(notACountry);

        // Assert
        assertFalse(result);
    }

    @Test
    void returnsFalseIfCountryNull() {
        // Arrange
        Country c = new Country("France", admin1);

        // Act
        boolean result = c.equals(null);

        // Assert
        assertFalse(result);
    }

}