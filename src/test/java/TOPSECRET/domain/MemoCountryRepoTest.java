package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoCountryRepoTest {
    private CountryFactory _countryFactory;

    @BeforeEach
    void setUp() {
        _countryFactory = new CountryFactory();
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        //Act
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
    }

    @Test
    void shouldRegisterCountrySuccessfully() {
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        Country result = memoCountryRepo.registerCountry("Portugal");
        //Assert
        assertNotNull(result);
        assertEquals("PORTUGAL", result.getCountryName());
    }

    @Test
    void shouldRegistersMultipleUniqueCountries() {
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        Country first = memoCountryRepo.registerCountry("Portugal");
        Country second = memoCountryRepo.registerCountry("Germany");
        //Assert
        assertNotNull(first);
        assertNotNull(second);
        assertEquals(2, memoCountryRepo.getAllCountries().size());
    }

    @Test
    void shouldReturnNullIfCountryIsDuplicate() {
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        Country first = memoCountryRepo.registerCountry("Portugal");
        Country Duplicate = memoCountryRepo.registerCountry("Portugal");
        //Assert
        assertNotNull(first);
        assertNull(Duplicate);
        assertEquals(1, memoCountryRepo.getAllCountries().size());
    }

    @Test
    void shouldReturnNullIfCountryNameDiffersOnlyByCaseOrSpaces() {
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        Country first = memoCountryRepo.registerCountry("Portugal");
        Country second = memoCountryRepo.registerCountry("portugal");
        Country third = memoCountryRepo.registerCountry(" Portugal ");
        //Assert
        assertNotNull(first);
        assertNull(second);
        assertNull(third);
        assertEquals(1, memoCountryRepo.getAllCountries().size());
    }

    @Test
    void shouldReturnsUnmodifiedListOfCountries() {
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        memoCountryRepo.registerCountry("Portugal");
        List<Country> countries = memoCountryRepo.getAllCountries();
        //Assert
        assertEquals(1, countries.size());
        assertThrows(UnsupportedOperationException.class, () -> countries.add(new Country("Germany")));
    }

    @Test
    void findByName_shouldReturnsNullWhenNameIsNull() {
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        memoCountryRepo.registerCountry("Portugal");
        //Assert - Optional expected
        assertFalse(memoCountryRepo.findByName(null).isPresent());
    }

    @Test
    void findByName_shouldFindCountryIgnoringCaseAndSpaces() {
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        Country portugal = memoCountryRepo.registerCountry("Portugal");
        //Assert - Optional expected
        assertEquals(portugal, memoCountryRepo.findByName("portugal").orElse(null));
        assertEquals(portugal, memoCountryRepo.findByName(" Portugal ").orElse(null));
    }

    @Test
    void findByName_shouldReturnNullWhenCountryNotFound() {
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        memoCountryRepo.registerCountry("Portugal");
        //Assert - Optional expected
        assertFalse(memoCountryRepo.findByName("Germany").isPresent());
    }

    @Test
    void ofIdentity_returnsOptionalWhenPresent() {
        // Use real factory and value objects for this test
        CountryFactory factory = new CountryFactory();
        MemoCountryRepo repo = new MemoCountryRepo(factory);

        Country country = factory.createCountry("PT", "Portugal");
        repo.save(country);

        java.util.Optional<Country> opt = repo.ofIdentity(new TOPSECRET.domain.valueobject.CountryId("PT"));
        assertTrue(opt.isPresent());
        assertEquals(country, opt.get());
        assertTrue(repo.containsOfIdentity(new TOPSECRET.domain.valueobject.CountryId("PT")));
    }
}