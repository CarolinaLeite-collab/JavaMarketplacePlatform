package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    private User admin1;
    private User admin2;

    @BeforeEach
    void setUp() {
        // Arrange
        Address address = new Address("Rua Dr.Amilcar de Castro", "24", Address.BuildingType.HOUSE, "Barcelos", "Braga", Address.Country.PORTUGAL, "4775-105", null);
        Phone phone = new Phone(new PhonePrefix("+351"), " 962064343 ");

        admin1 = new User(new Name("Marcelo"), address, new Email("1251995@isep.ipp.pt"), phone);
        admin2 = new User(new Name("Marcio"), address, new Email("1251985@isep.ipp.pt"), phone);
    }

    @Test
    void should_returnTrue_forsameName() {
        // Act
        Country c1 = new Country("France", admin1, LocalDate.of(2020, 1, 1));
        Country c2 = new Country("France", admin2, LocalDate.of(2023, 5, 10));

        // Assert
        assertEquals(c1, c2);
    }

    @Test
    void should_returnTrue_forDifferentName() {
        // Act
        Country c1 = new Country("France", admin1, LocalDate.of(2020, 1, 1));
        Country c2 = new Country("Germany", admin2, LocalDate.of(2020, 1, 1));

        // Assert
        assertNotEquals(c1, c2);
    }

    @Test
    void equals_shouldReturnFalse_whenObjectIsNotCountry() {
        // Arrange
        Country c = new Country("France", admin1, LocalDate.now());
        Object notACountry = "Not a Country";

        // Act
        boolean result = c.equals(notACountry);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenObjectIsNull() {
        // Arrange
        Country c = new Country("France", admin1, LocalDate.now());

        // Act
        boolean result = c.equals(null);

        // Assert
        assertFalse(result);
    }

}