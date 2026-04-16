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

    @BeforeEach
    void setUp() {
        // Arrange
        _countryFactoryDouble = mock(CountryFactory.class);
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        // SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactoryDouble);

        // Assert
        assertNotNull(memoCountryRepo);
    }

    @Test
    void shouldRegisterCountrySuccessfully() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);
        Country countryDouble = mock(Country.class);
        when(countryDouble.identity()).thenReturn(countryIdDouble);
        when(_countryFactoryDouble.createCountry("Portugal")).thenReturn(countryDouble);

        // SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        Country result = memoCountryRepo.addCountry("Portugal");

        // Assert
        assertNotNull(result);
        assertSame(countryDouble, result);
    }

    @Test
    void addCountryDuplicateThrowsIllegalArgumentException() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);
        Country countryDouble = mock(Country.class);
        when(countryDouble.identity()).thenReturn(countryIdDouble);
        when(_countryFactoryDouble.createCountry("Portugal")).thenReturn(countryDouble);

        // SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        memoCountryRepo.addCountry("Portugal");

        // Assert
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class,
                () -> memoCountryRepo.addCountry("Portugal"));
        assertEquals("Country already exists in the repository", result.getMessage());
    }

    @Test
    void saveValidCountryReturnsCountry() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);
        Country countryDouble = mock(Country.class);
        when(countryDouble.identity()).thenReturn(countryIdDouble);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        Country result = repo.save(countryDouble);

        // Assert
        assertSame(countryDouble, result);
    }

    @Test
    void ofIdentityExistingCountryIdReturnsCountry() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);
        Country countryDouble = mock(Country.class);
        when(countryDouble.identity()).thenReturn(countryIdDouble);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        repo.save(countryDouble);
        Optional<Country> result = repo.ofIdentity(countryIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(countryDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingCountryIdReturnsEmpty() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        Optional<Country> result = repo.ofIdentity(countryIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityExistingCountryIdReturnsTrue() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);
        Country countryDouble = mock(Country.class);
        when(countryDouble.identity()).thenReturn(countryIdDouble);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        repo.save(countryDouble);
        boolean result = repo.containsOfIdentity(countryIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingCountryIdReturnsFalse() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        boolean result = repo.containsOfIdentity(countryIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void findAllReturnsTwoStoredCountries() {
        // Arrange
        CountryId countryId1Double = mock(CountryId.class);
        Country country1Double = mock(Country.class);
        when(country1Double.identity()).thenReturn(countryId1Double);

        CountryId countryId2Double = mock(CountryId.class);
        Country country2Double = mock(Country.class);
        when(country2Double.identity()).thenReturn(countryId2Double);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        repo.save(country1Double);
        repo.save(country2Double);
        Iterable<Country> result = repo.findAll();

        // Assert
        List<Country> list = new ArrayList<>();
        result.forEach(list::add);
        assertEquals(2, list.size());
    }

    @Test
    void findAllEmptyRepoReturnsEmptyIterable() {
        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        Iterable<Country> result = repo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }
    @Test
    void findAllKeysReturnsStoredIds() {
        // Arrange
        CountryId countryId1Double = mock(CountryId.class);
        Country country1Double = mock(Country.class);
        when(country1Double.identity()).thenReturn(countryId1Double);

        CountryId countryId2Double = mock(CountryId.class);
        Country country2Double = mock(Country.class);
        when(country2Double.identity()).thenReturn(countryId2Double);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);
        repo.save(country1Double);
        repo.save(country2Double);

        // Act
        List<CountryId> result = repo.findAllKeys();

        // Assert
        assertEquals(2, result.size(), "Should contain exactly 2 keys");
        assertTrue(result.contains(countryId1Double), "Should contain the first country ID");
        assertTrue(result.contains(countryId2Double), "Should contain the second country ID");
    }

    @Test
    void findAllKeysEmptyRepoReturnsEmptyList() {
        // Arrange
        // (Nothing to arrange specifically for an empty repo)

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        List<CountryId> result = repo.findAllKeys();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "List should be empty when no countries are saved");
    }
}