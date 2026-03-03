package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.configuration.IMockitoConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterCountryControllerTest {
    private CountryFactory _factory;
    private CountryRepo _countryRepo;
    private RegisterCountryController _controller;
    private User _admin;

    @BeforeEach
    void setUp() throws InstantiationException {
        // Arrange
        _admin = mock(User.class);
        Country country1 = mock(Country.class);
        when(country1.getCountryName()).thenReturn("Portugal");
        _factory = mock(CountryFactory.class);
        _countryRepo = mock(CountryRepo.class);
        _controller = new RegisterCountryController(_countryRepo);
    }

    @Test
    void constructsControllerSuccessfully() {
        //arrange
        //SUT
        _controller = new RegisterCountryController(_countryRepo);
        // act and assert
        assertNotNull(_controller);
    }

    @Test
    void throwsExceptionNullCountryRepo() {
        // act and assert
        assertThrows(NullPointerException.class, () ->
                new RegisterCountryController(null));

    }
    /*   @Test
    void shouldRegisterCountrySuccessfully() throws InstantiationException {

        // Act
        Country country = _controller.registerCountry("Portugal");

        // Assert
        assertNotNull(country);

        // asserts a efeitos secundários
        assertEquals("Portugal", country.getCountryName());
        //assertEquals(1, _countryRepo.getAllCountries().size());
        //assertEquals(country, _countryRepo.getAllCountries().get(0));
    }

 @Test
    void shouldNotRegisterCountrySuccessfully() throws InstantiationException {
        //Arrange
        Country country = _countryRepo.registerCountry("Portugal");
        List<Country> countries = _countryRepo.getAllCountries();

        // Act
        Country duplicate = _controller.registerCountry("Portugal");

        // Assert
        assertNull(duplicate);
        assertEquals(1, countries.size());
    }

    @Test
    void shouldNotRegisterCountryInvalidName() {
        // Arrange
        String empty = "";
        // Act and Assert
        Assertions.assertThrows(InstantiationException.class, () -> _countryRepo.registerCountry(empty));
    }*/
}