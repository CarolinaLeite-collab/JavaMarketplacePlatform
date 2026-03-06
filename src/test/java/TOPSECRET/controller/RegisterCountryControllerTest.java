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
    private RegisterCountryController _sut;
    private User _admin;

    @BeforeEach
    void setUp() throws InstantiationException {

        _countryRepo = mock(CountryRepo.class);
        _sut = new RegisterCountryController(_countryRepo);
    }

    @Test
    void constructsControllerSuccessfully() {
        // act and assert
        assertNotNull(_sut);
    }

    @Test
    void throwsExceptionNullCountryRepo() {
        // act and assert
        assertThrows(NullPointerException.class, () ->
                new RegisterCountryController(null));

    }

    @Test
    void shouldRegisterCountrySuccessfully() throws InstantiationException {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);
        Country portugal = mock(Country.class);
        when(_countryRepo.registerCountry("Portugal")).thenReturn(portugal);
        //Act
        Country country = _sut.registerCountry("Portugal", user);

        // Assert
        assertNotNull(country);

        // asserts a efeitos secundários
        //assertEquals("PORTUGAL", country.getCountryName());
        //assertEquals(1, _countryRepo.getAllCountries().size());
        //assertEquals(country, _countryRepo.getAllCountries().get(0));
    }

    @Test
    void shouldNotRegisterCountrySuccessfullyIfUserNotAdmin() throws SecurityException {
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(false);
        List<Country> countries = _countryRepo.getAllCountries();

        // Act & Assert
        Assertions.assertThrows(SecurityException.class, () -> _sut.registerCountry("Portugal", user));
    }

    @Test
    void shouldNotRegisterNullUser() throws InstantiationException {
        // Arrange
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> _sut.registerCountry("Portugal", null));
    }
}