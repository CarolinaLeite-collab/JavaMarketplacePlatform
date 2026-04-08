package TOPSECRET.persistence.mem;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.country.CountryFactory;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;
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
        
        _countryFactoryDouble = mock(CountryFactory.class);
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        //Act
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactoryDouble);
    }

    @Test
    void shouldRegisterCountrySuccessfully() {
        // Arrange
        CountryId _countryIdDouble = new CountryId("PT");
        Country _countryDouble = mock(Country.class);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);
        when(_countryFactoryDouble.createCountry("PT", "Portugal")).thenReturn(_countryDouble);

        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactoryDouble);

        //Act
        Country result = memoCountryRepo.addCountry("PT", "Portugal");

        //Assert
        assertNotNull(result);
        assertSame(_countryDouble, result);
    }


    @Test
    void addCountryDuplicateThrowsIllegalArgumentException() {

        // Arrange
        CountryId _countryIdDouble = new CountryId("PT");
        Country _countryDouble = mock(Country.class);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);
        when(_countryFactoryDouble.createCountry("PT", "Portugal")).thenReturn(_countryDouble);

        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactoryDouble);

        // Act
        memoCountryRepo.addCountry("PT", "Portugal");

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                memoCountryRepo.addCountry("PT", "Portugal"));

    }

    @Test
    void saveValidCountryReturnsCountry() {
        // Arrange
        CountryId _countryIdDouble = mock(CountryId.class);
        Country _countryDouble = mock(Country.class);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);


        Country result = repo.save(_countryDouble);

        // Assert
        assertSame(_countryDouble, result);
    }

    @Test
    void ofIdentityExistingCountryIdReturnsCountry() {
        // Arrange
        CountryId _countryIdDouble = mock(CountryId.class);
        Country _countryDouble = mock(Country.class);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        repo.save(_countryDouble);
        Optional<Country> result = repo.ofIdentity(_countryIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(_countryDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingCountryIdReturnsEmpty() {
        // Arrange
        CountryId _countryIdDouble = mock(CountryId.class);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);


        Optional<Country> result = repo.ofIdentity(_countryIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityExistingCountryIdReturnsTrue() {
        // Arrange
        CountryId _countryIdDouble = mock(CountryId.class);
        Country _countryDouble = mock(Country.class);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);

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
        CountryId _countryIdDouble = mock(CountryId.class);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);


        boolean result = repo.containsOfIdentity(_countryIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void findAllReturnsTwoStoredCountries() {
        // Arrange
        CountryId _countryId1Double = mock(CountryId.class);
        CountryId _countryId2Double = mock(CountryId.class);
        Country _country1Double = mock(Country.class);
        Country _country2Double = mock(Country.class);
        when(_country1Double.identity()).thenReturn(_countryId1Double);
        when(_country2Double.identity()).thenReturn(_countryId2Double);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

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
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // SUT
        Iterable<Country> result = repo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void findByNameExistingNameReturnsCountry() {
        // Arrange
        CountryId _countryIdDouble = mock(CountryId.class);
        Country _countryDouble = mock(Country.class);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);
        when(_countryDouble.isNamed(new CountryName("Portugal"))).thenReturn(true);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        repo.save(_countryDouble);
        Optional<Country> result = repo.findByName("Portugal");

        // Assert
        assertTrue(result.isPresent());
        assertSame(_countryDouble, result.get());
    }

    @Test
    void findByNameNonExistingNameReturnsEmpty() {
        // Arrange
        CountryId _countryIdDouble = mock(CountryId.class);
        Country _countryDouble = mock(Country.class);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);
        when(_countryDouble.isNamed(new CountryName("Portugal"))).thenReturn(true);

        // SUT
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        repo.save(_countryDouble);
        Optional<Country> result = repo.findByName("Deutschland");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByNameNullReturnsEmpty() {
        // Arrange
        MemoCountryRepo repo = new MemoCountryRepo(_countryFactoryDouble);

        // SUT
        Optional<Country> result = repo.findByName(null);

        // Assert
        assertTrue(result.isEmpty());
    }
}