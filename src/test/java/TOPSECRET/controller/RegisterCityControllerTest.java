package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCityControllerTest {

    private CityRepo _cityRepo;
    private CountryRepo _countryRepo;
    private RegisterCityController _sut;
    private Country _portugal;
    private CountryFactory _countryFactory;

    @BeforeEach
    void setUp() throws InstantiationException {
        _cityRepo = new CityRepo();
        _countryFactory = new CountryFactory();
        _countryRepo = new CountryRepo(_countryFactory);
        _portugal = _countryRepo.registerCountry("Portugal");
        _sut = new RegisterCityController(_cityRepo, _countryRepo);
    }

    @Test
    void registerCity_happyPath() {
        // Act
        City c = _sut.registerCity("Porto", _portugal);

        // Assert
        assertNotNull(c);
        assertEquals("Porto", c.getName());
        assertEquals(_portugal, c.getCountry());
    }

    @Test
    void registerCity_duplicateThrows() {
        // Arrange
        _sut.registerCity("Porto", _portugal);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> _sut.registerCity("Porto", _portugal));
    }

    @Test
    void registerCity_nullCountryThrows() {
        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> _sut.registerCity("Porto", null));
        assertEquals("Country cannot be null", error.getMessage());
    }

    @Test
    void registerCity_blankOrNullCityNameThrows() {
        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> _sut.registerCity("", _portugal));
        assertEquals("City name cannot be null or blank", error.getMessage());
    }

    @Test
    void registerCity_trimmedDuplicateNameThrows() {
        // Arrange
        _sut.registerCity("Porto", _portugal);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> _sut.registerCity(" Porto ", _portugal));
    }

    // Tests that getCountries() returns the actual list from the repo and not an empty list.
    @Test void getCountries_returnsAllCountriesFromRepo() {
        // Arrange
        _countryRepo.registerCountry("Spain");

        // Act
        List<Country> result = _sut.getCountries();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getCountryName().equals("PORTUGAL")));
        assertTrue(result.stream().anyMatch(c -> c.getCountryName().equals("SPAIN"))); }
}
