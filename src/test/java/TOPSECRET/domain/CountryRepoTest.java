package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryRepoTest {

    private CountryRepo countryRepo;
    private User admin;

    @BeforeEach
    void setUp() {
        // Arrange
        countryRepo = new CountryRepo();
        Phone phone = new Phone(new PhonePrefix("+351"), "909978798");
        Address address = new Address(
                "Rua Dr. Rui Falcão", "33",
                Address.BuildingType.HOUSE,
                "Barcelos", "Braga",
                Address.Country.PORTUGAL,
                "4790-105", null
        );
        admin = new User(new Name("Marcelo"), address, new Email("test@test.pt"), phone);
    }

    @Test
    void constructsRepoSuccessfully() {
        // Act
        CountryRepo repo = new CountryRepo();

        // Assert
        assertNotNull(repo);
        assertEquals(0, repo.getAllCountries().size());
    }

    @Test
    void registerCountrySuccessfully() {
        // Act
        Country portugal = countryRepo.registerCountry("Portugal", admin);

        // Assert
        assertNotNull(portugal);
        assertEquals(1, countryRepo.getAllCountries().size());
        assertEquals(portugal, countryRepo.getAllCountries().get(0));
    }

    @Test
    void registersMultipleUniqueCountries() {
        // Act
        Country portugal = countryRepo.registerCountry("Portugal", admin);
        Country germany = countryRepo.registerCountry("Germany", admin);

        // Assert
        assertNotNull(portugal);
        assertNotNull(germany);
        assertEquals(2, countryRepo.getAllCountries().size());
    }

    @Test
    void returnNullIfCountryDuplicate() {
        // Act
        Country portugal = countryRepo.registerCountry("Portugal", admin);
        Country portugalDuplicate = countryRepo.registerCountry("Portugal", admin);

        // Assert
        assertNotNull(portugal);
        assertNull(portugalDuplicate);
        assertEquals(1, countryRepo.getAllCountries().size());
    }

    @Test
    void returnNullIfCountryNameDiffersOnlyByCaseOrSpaces() {
        // Act
        Country portugal = countryRepo.registerCountry("Portugal", admin);
        Country portugal1 = countryRepo.registerCountry("portugal", admin);
        Country portugal2 = countryRepo.registerCountry(" Portugal ", admin);

        // Assert
        assertNotNull(portugal);
        assertNull(portugal1);
        assertNull(portugal2);
        assertEquals(1, countryRepo.getAllCountries().size());
    }

    @Test
    void returnsUnmodifiableListOfCountries() {
        Country portugal = countryRepo.registerCountry("Portugal", admin);
        List<Country> countries = countryRepo.getAllCountries();

        // Assert
        assertEquals(1, countries.size());

        assertThrows(UnsupportedOperationException.class, () -> countries.add(new Country("Germany", admin)));
    }

}