package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoCountryRepoTest {
    private CountryFactory _countryFactory;

    @BeforeEach
    void setUp() {
        _countryFactory = mock(CountryFactory.class);
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        //Act
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
    }

    @Test
    void shouldRegisterCountrySuccessfully() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        Country result = memoCountryRepo.registerCountry("Portugal");
        //Assert
        assertEquals(portugal, result);
    }

    @Test
    void shouldRegistersMultipleUniqueCountries() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);

        Country germany = mock(Country.class);
        when(_countryFactory.createCountry("Germany")).thenReturn(germany);
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
    void shouldReturnNullIfCountryIsDuplicate() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal, portugal);
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
    void shouldReturnNullIfCountryNameDiffersOnlyByCaseOrSpaces() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(portugal.isNamed("PORTUGAL")).thenReturn(true);

        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
        when(_countryFactory.createCountry("portugal")).thenReturn(portugal);
        when(_countryFactory.createCountry(" Portugal ")).thenReturn(portugal);
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
    void shouldReturnsUnmodifiedListOfCountries() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
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
    void findByName_shouldReturnsNullWhenNameIsNull() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
        when(portugal.isNamed("PORTUGAL")).thenReturn(true);
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        memoCountryRepo.registerCountry("Portugal");
        //Assert
        assertNull(memoCountryRepo.findByName(null));
    }

    @Test
    void findByName_shouldFindCountryIgnoringCaseAndSpaces() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
        when(portugal.isNamed("PORTUGAL")).thenReturn(true);
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        memoCountryRepo.registerCountry("Portugal");
        //Assert
        assertEquals(portugal, memoCountryRepo.findByName("portugal"));
        assertEquals(portugal, memoCountryRepo.findByName(" Portugal "));
    }

    @Test
    void findByName_shouldReturnNullWhenCountryNotFound() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
        when(portugal.isNamed("PORTUGAL")).thenReturn(true);
        //SUT
        MemoCountryRepo memoCountryRepo = new MemoCountryRepo(_countryFactory);
        //Act
        memoCountryRepo.registerCountry("Portugal");
        //Assert
        assertNull(memoCountryRepo.findByName("Germany"));
    }
}