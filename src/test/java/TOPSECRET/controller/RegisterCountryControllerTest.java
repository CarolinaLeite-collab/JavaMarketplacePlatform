package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterCountryControllerTest {
    private CountryRepo _countryRepo;
    private RegisterCountryController _sut;

    @BeforeEach
    void setUp(){
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
    void shouldRegisterCountrySuccessfully(){
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(true);
        Country portugal = mock(Country.class);
        when(_countryRepo.registerCountry("Portugal")).thenReturn(portugal);
        //Act
        Country country = _sut.registerCountry("Portugal", user);
        // Assert
        assertNotNull(country);
    }

    @Test
    void shouldNotRegisterCountrySuccessfullyIfUserNotAdmin(){
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(false);
        // Act & Assert
        Assertions.assertThrows(SecurityException.class, () -> _sut.registerCountry("Portugal", user));
    }

    @Test
    void shouldNotRegisterNullUser(){
        // Arrange
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> _sut.registerCountry("Portugal", null));
    }
}