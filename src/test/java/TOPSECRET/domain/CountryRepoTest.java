package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountryRepoTest {
    private CountryFactory _countryFactory;

    @BeforeEach
    void setUp() {
        _countryFactory = mock(CountryFactory.class);
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        //Act
        //SUT
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
    }

    @Test
    void shouldRegisterCountrySuccessfully() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
        //SUT
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        Country result = countryRepo.registerCountry("Portugal");
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
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        Country first = countryRepo.registerCountry("Portugal");
        Country second = countryRepo.registerCountry("Germany");
        //Assert
        assertNotNull(first);
        assertNotNull(second);
        assertEquals(2, countryRepo.getAllCountries().size());
    }

    @Test
    void shouldReturnNullIfCountryIsDuplicate() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal, portugal);
        //SUT
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        Country first = countryRepo.registerCountry("Portugal");
        Country Duplicate = countryRepo.registerCountry("Portugal");
        //Assert
        assertNotNull(first);
        assertNull(Duplicate);
        assertEquals(1, countryRepo.getAllCountries().size());
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
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        Country first = countryRepo.registerCountry("Portugal");
        Country second = countryRepo.registerCountry("portugal");
        Country third = countryRepo.registerCountry(" Portugal ");
        //Assert
        assertNotNull(first);
        assertNull(second);
        assertNull(third);
        assertEquals(1, countryRepo.getAllCountries().size());
    }

    @Test
    void shouldReturnsUnmodifiedListOfCountries() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
        //SUT
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        countryRepo.registerCountry("Portugal");
        List<Country> countries = countryRepo.getAllCountries();
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
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        countryRepo.registerCountry("Portugal");
        //Assert
        assertNull(countryRepo.findByName(null));
    }

    @Test
    void findByName_shouldFindCountryIgnoringCaseAndSpaces() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
        when(portugal.isNamed("PORTUGAL")).thenReturn(true);
        //SUT
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        countryRepo.registerCountry("Portugal");
        //Assert
        assertEquals(portugal, countryRepo.findByName("portugal"));
        assertEquals(portugal, countryRepo.findByName(" Portugal "));
    }

    @Test
    void findByName_shouldReturnNullWhenCountryNotFound() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createCountry("Portugal")).thenReturn(portugal);
        when(portugal.isNamed("PORTUGAL")).thenReturn(true);
        //SUT
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        countryRepo.registerCountry("Portugal");
        //Assert
        assertNull(countryRepo.findByName("Germany"));
    }
}