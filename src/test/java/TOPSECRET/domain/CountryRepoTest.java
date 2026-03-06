package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountryRepoTest {
    private CountryRepo _sut;
    private CountryFactory _countryFactory;

    @BeforeEach
    void setUp() {
        _countryFactory = mock(CountryFactory.class);
        _sut = new CountryRepo(_countryFactory);
    }

    @Test
    void shouldConstructRepoSuccessfully() {
        // Assert
        assertNotNull(_sut);
        assertEquals(0, _sut.getAllCountries().size());
    }

    @Test
    void shouldRegisterCountrySuccessfully() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        // Act
        Country result = _sut.registerCountry("Portugal");

        // Assert
        assertNotNull(result);
        assertEquals(1, _sut.getAllCountries().size());
        assertEquals(portugal, _sut.getAllCountries().get(0));
    }

    @Test
    void shouldRegistersMultipleUniqueCountries() throws InstantiationException {
        // Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);

        Country germany = mock(Country.class);
        when(_countryFactory.createClass("Germany")).thenReturn(germany);

        // Act
        Country first = _sut.registerCountry("Portugal");
        Country second = _sut.registerCountry("Germany");

        // Assert
        assertNotNull(first);
        assertNotNull(second);
        assertEquals(2, _sut.getAllCountries().size());
    }

    @Test
    void shouldReturnNullIfCountryIsDuplicate() {
        // Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal, portugal);
        // Act
        Country first = _sut.registerCountry("Portugal");
        Country Duplicate = _sut.registerCountry("Portugal");
        // Assert
        assertNotNull(first);
        assertNull(Duplicate);
        assertEquals(1, _sut.getAllCountries().size());
    }

    @Test
    void shouldReturnNullIfCountryNameDiffersOnlyByCaseOrSpaces() {
        // Arrange
        Country portugal = mock(Country.class);
        when(portugal.getCountryName()).thenReturn("PORTUGAL");

        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        when(_countryFactory.createClass("portugal")).thenReturn(portugal);
        when(_countryFactory.createClass(" Portugal ")).thenReturn(portugal);
        // Act
        Country first = _sut.registerCountry("Portugal");
        Country second = _sut.registerCountry("portugal");
        Country third = _sut.registerCountry(" Portugal ");
        // Assert
        assertNotNull(first);
        assertNull(second);
        assertNull(third);
        assertEquals(1, _sut.getAllCountries().size());
    }

    @Test
    void shouldReturnsUnmodifiedListOfCountries() {
        // Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        // Act
        _sut.registerCountry("Portugal");
        List<Country> countries = _sut.getAllCountries();

        // Assert
        assertEquals(1, countries.size());
        assertThrows(UnsupportedOperationException.class, () -> countries.add(new Country("Germany")));
    }

    //Test findByName() method
    @Test
    void findByName_shouldReturnsNullWhenNameIsNull() {
        // Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        when(portugal.getCountryName()).thenReturn("PORTUGAL");
        // Act
        _sut.registerCountry("Portugal");
        // Assert
        assertNull(_sut.findByName(null));
    }

    @Test
    void findByName_shouldFindsCountryIgnoringCaseAndSpaces() {
        // Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        when(portugal.getCountryName()).thenReturn("PORTUGAL");
        // Act
        _sut.registerCountry("Portugal");
        // Assert
        assertEquals(portugal, _sut.findByName("portugal"));
        assertEquals(portugal, _sut.findByName(" Portugal "));
    }

    @Test
    void findByName_shouldReturnNullWhenCountryNotFound() {
        // Arrange
        Country portugal = mock(Country.class);
        when(_countryFactory.createClass("Portugal")).thenReturn(portugal);
        when(portugal.getCountryName()).thenReturn("PORTUGAL");
        // Act
        _sut.registerCountry("Portugal");
        // Assert
        assertNull(_sut.findByName("Germany"));
    }

}