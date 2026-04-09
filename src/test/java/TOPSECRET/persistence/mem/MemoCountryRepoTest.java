package TOPSECRET.persistence.mem;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.country.CountryFactory;
import TOPSECRET.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoCountryRepoTest {
    private CountryFactory _countryFactoryDouble;
    private int _countryIdIndex;
    private static final String[] ISO_CODES = {"PT", "ES", "FR", "DE", "IT", "GB", "US"};

    @BeforeEach
    void setUp() {
        _countryFactoryDouble = mock(CountryFactory.class);
        _countryIdIndex = 0;
    }

    private CountryId createCountryId() {
        String iso = ISO_CODES[_countryIdIndex % ISO_CODES.length];
        _countryIdIndex++;
        return new CountryId(iso);
    }

    private Country createCountryDouble(CountryId countryIdDouble) {
        Country _countryDouble = mock(Country.class);
        when(_countryDouble.identity()).thenReturn(countryIdDouble);
        return _countryDouble;
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        // Arrange

        // SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactoryDouble);

        // Act

        // Assert
        assertNotNull(memoCountryRepo);
    }

    @Test
    void shouldRegisterCountrySuccessfully() {
        // Arrange
        CountryId _countryIdDouble = createCountryId();
        Country _countryDouble = createCountryDouble(_countryIdDouble);
        when(_countryFactoryDouble.createCountry("Portugal")).thenReturn(_countryDouble);

        // SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        Country result = memoCountryRepo.addCountry("Portugal");

        // Assert
        assertNotNull(result);
        assertSame(_countryDouble, result);
    }


    @Test
    void addCountryDuplicateThrowsIllegalArgumentException() {

        // Arrange
        CountryId _countryIdDouble = createCountryId();
        Country _countryDouble = createCountryDouble(_countryIdDouble);
        when(_countryFactoryDouble.createCountry("Portugal")).thenReturn(_countryDouble);

        // SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        memoCountryRepo.addCountry("Portugal");

        IllegalArgumentException result = assertThrows(IllegalArgumentException.class,
                () -> memoCountryRepo.addCountry("Portugal"));

        // Assert
        assertEquals("Country already exists in the repository", result.getMessage());
    }

    @Test
    void saveValidCountryReturnsCountry() {
        // Arrange
        CountryId _countryIdDouble = createCountryId();
        Country _countryDouble = createCountryDouble(_countryIdDouble);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        Country result = repo.save(_countryDouble);

        // Assert
        assertSame(_countryDouble, result);
    }

    @Test
    void ofIdentityExistingCountryIdReturnsCountry() {
        // Arrange
        CountryId _countryIdDouble = createCountryId();
        Country _countryDouble = createCountryDouble(_countryIdDouble);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        repo.save(_countryDouble);
        Optional<Country> result = repo.ofIdentity(_countryIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(_countryDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingCountryIdReturnsEmpty() {
        // Arrange
        CountryId _countryIdDouble = createCountryId();

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        Optional<Country> result = repo.ofIdentity(_countryIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityExistingCountryIdReturnsTrue() {
        // Arrange
        CountryId _countryIdDouble = createCountryId();
        Country _countryDouble = createCountryDouble(_countryIdDouble);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        repo.save(_countryDouble);
        boolean result = repo.containsOfIdentity(_countryIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingCountryIdReturnsFalse() {
        // Arrange
        CountryId _countryIdDouble = createCountryId();

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        boolean result = repo.containsOfIdentity(_countryIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void findAllReturnsTwoStoredCountries() {
        // Arrange
        CountryId _countryId1Double = createCountryId();
        CountryId _countryId2Double = createCountryId();
        Country _country1Double = createCountryDouble(_countryId1Double);
        Country _country2Double = createCountryDouble(_countryId2Double);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        repo.save(_country1Double);
        repo.save(_country2Double);
        Iterable<Country> result = repo.findAll();

        // Assert
        List<Country> list = new ArrayList<>();
        result.forEach(list::add);
        assertEquals(2, list.size());
    }

    @Test
    void findAllEmptyRepoReturnsEmptyIterable() {
        // Arrange

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        Iterable<Country> result = repo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }
}