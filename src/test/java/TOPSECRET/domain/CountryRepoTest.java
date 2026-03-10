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
        //Assert
    }

    @Test
    void shouldRegisterCountrySuccessfully() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
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
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);

        Country germany = mock(Country.class);
        when(_countryFactory.createClass("Germany")).thenReturn(germany);
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
    void shouldReturnNullIfCountryIsDuplicate() {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal, portugal);
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
    void shouldReturnNullIfCountryNameDiffersOnlyByCaseOrSpaces() {
        //Arrange
        Country portugal = mock(Country.class);
        when(portugal.isNamed("PORTUGAL")).thenReturn(true);

        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        when(_countryFactory.createClass("portugal")).thenReturn(portugal);
        when(_countryFactory.createClass(" Portugal ")).thenReturn(portugal);
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
    void shouldReturnsUnmodifiedListOfCountries() {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        //SUT
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        countryRepo.registerCountry("Portugal");
        List<Country> countries = countryRepo.getAllCountries();
        //Assert
        assertEquals(1, countries.size());
        assertThrows(UnsupportedOperationException.class, () -> countries.add(new Country("Germany")));
    }

    //Test findByName() method
    @Test
    void findByName_shouldReturnsNullWhenNameIsNull() {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        when(portugal.isNamed("PORTUGAL")).thenReturn(true);
        //SUT
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        countryRepo.registerCountry("Portugal");
        //Assert
        assertNull(countryRepo.findByName(null));
    }

    @Test
    void findByName_shouldFindCountryIgnoringCaseAndSpaces() {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
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
    void findByName_shouldReturnNullWhenCountryNotFound() {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        when(portugal.isNamed("PORTUGAL")).thenReturn(true);
        //SUT
        CountryRepo countryRepo = new CountryRepo(_countryFactory);
        //Act
        countryRepo.registerCountry("Portugal");
        //Assert
        assertNull(countryRepo.findByName("Germany"));
    }
}