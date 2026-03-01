package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryRepoTest {

    @Test
    void constructsRepoSuccessfully() {
        // Arrange
        CountryRepo countryRepo = new CountryRepo();

        // Act
        CountryRepo repo = new CountryRepo();

        // Assert
        assertNotNull(repo);
        assertEquals(0, repo.getAllCountries().size());
    }

    @Test
    void registerCountrySuccessfully() throws InstantiationException {
        // Arrange
        CountryRepo countryRepo = new CountryRepo();

        // Act
        Country portugal = countryRepo.registerCountry("Portugal");

        // Assert
        assertNotNull(portugal);
        assertEquals(1, countryRepo.getAllCountries().size());
        assertEquals(portugal, countryRepo.getAllCountries().get(0));
    }

    @Test
    void registersMultipleUniqueCountries() throws InstantiationException {
        // Arrange
        CountryRepo countryRepo = new CountryRepo();

        // Act
        Country portugal = countryRepo.registerCountry("Portugal");
        Country germany = countryRepo.registerCountry("Germany");

        // Assert
        assertNotNull(portugal);
        assertNotNull(germany);
        assertEquals(2, countryRepo.getAllCountries().size());
    }

    @Test
    void returnNullIfCountryDuplicate() throws InstantiationException {
        // Arrange
        CountryRepo countryRepo = new CountryRepo();

        // Act
        Country portugal = countryRepo.registerCountry("Portugal");
        Country portugalDuplicate = countryRepo.registerCountry("Portugal");

        // Assert
        assertNotNull(portugal);
        assertNull(portugalDuplicate);
        assertEquals(1, countryRepo.getAllCountries().size());
    }

    @Test
    void returnNullIfCountryNameDiffersOnlyByCaseOrSpaces() throws InstantiationException {
        // Arrange
        CountryRepo countryRepo = new CountryRepo();

        // Act
        Country portugal = countryRepo.registerCountry("Portugal");
        Country portugal1 = countryRepo.registerCountry("portugal");
        Country portugal2 = countryRepo.registerCountry(" Portugal ");

        // Assert
        assertNotNull(portugal);
        assertNull(portugal1);
        assertNull(portugal2);
        assertEquals(1, countryRepo.getAllCountries().size());
    }

    @Test
    void returnsUnmodifiableListOfCountries() throws InstantiationException {
        // Arrange
        CountryRepo countryRepo = new CountryRepo();

        // Act
        Country portugal = countryRepo.registerCountry("Portugal");
        List<Country> countries = countryRepo.getAllCountries();

        // Assert
        assertEquals(1, countries.size());
        assertThrows(UnsupportedOperationException.class, () -> countries.add(new Country("Germany")));
    }

    //Test findByName() method
    @Test
    void findByName_returnsNullWhenNameIsNull() throws InstantiationException {
        CountryRepo repo = new CountryRepo();
        repo.registerCountry("Portugal");

        assertNull(repo.findByName(null));
    }

    @Test
    void findByName_findsCountryIgnoringCaseAndSpaces() throws InstantiationException {
        CountryRepo repo = new CountryRepo();
        Country portugal = repo.registerCountry("Portugal");

        assertEquals(portugal, repo.findByName("portugal"));
        assertEquals(portugal, repo.findByName(" Portugal "));
    }

    @Test
    void findByName_returnsNullWhenCountryNotFound() throws InstantiationException {
        CountryRepo repo = new CountryRepo();
        repo.registerCountry("Portugal");

        assertNull(repo.findByName("Germany"));
    }

}