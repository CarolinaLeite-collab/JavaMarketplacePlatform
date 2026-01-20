package TOPSECRET.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryRepoTest {

    @Test
    void testRegisterCountry() {
        // Arrange
        Address address = new Address("Rua Dr.Amilcar de Castro", "24", Address.BuildingType.HOUSE, "Barcelos", "Braga", Address.Country.PORTUGAL, "4775-105", null);
        Phone phone = new Phone(new PhonePrefix("+351"), " 962064343 ");
        User admin = new User(new Name("Marcelo"), address, new Email("1251995@isep.ipp.pt"), phone);

        CountryRepo repo = new CountryRepo();

        // Act
        Country portugal = repo.registerCountry("Portugal", admin, LocalDate.now());
        Country portugalDuplicate = repo.registerCountry("Portugal", admin, LocalDate.now());
        Country germany = repo.registerCountry("Germany", admin, LocalDate.now());

        // Assert
        assertNotNull(portugal, "Portugal should be registered successfully");
        assertNull(portugalDuplicate, "Duplicate Portugal should not be registered");
        assertNotNull(germany, "Germany should be registered successfully");

        assertEquals(2, repo.getAllCountries().size(), "Repo should only contain exactly 2 countries");
    }

}