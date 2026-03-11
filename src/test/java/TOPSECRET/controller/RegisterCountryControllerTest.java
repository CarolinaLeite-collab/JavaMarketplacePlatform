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

    @BeforeEach
    void setUp(){
        _countryRepo = mock(CountryRepo.class);
    }

    @Test
    void constructsControllerSuccessfully() {
        //Act
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_countryRepo);
        //Assert
        assertNotNull(controller);
    }

    @Test
    void throwsExceptionNullCountryRepo() {
        //Assert
        assertThrows(NullPointerException.class, () ->
                new RegisterCountryController(null));
    }

    @Test
    void shouldRegisterCountrySuccessfully(){
        //Arrange
        User userDouble = mock(User.class);
        when(userDouble.hasRole(Role.ADMIN)).thenReturn(true);
        Country portugalDouble = mock(Country.class);
        when(_countryRepo.registerCountry("Portugal")).thenReturn(portugalDouble);
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_countryRepo);
        //Act
        Country country = controller.registerCountry("Portugal", userDouble);
        // Assert
        assertNotNull(country);
    }

    @Test
    void shouldNotRegisterCountrySuccessfullyIfUserNotAdmin(){
        //Arrange
        User userDouble = mock(User.class);
        when(userDouble.hasRole(Role.ADMIN)).thenReturn(false);
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_countryRepo);
        //Act & Assert
        Assertions.assertThrows(SecurityException.class, () -> controller.registerCountry("Portugal", userDouble));
    }

    @Test
    void shouldNotRegisterNullUser(){
        //Arrange
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_countryRepo);
        //Act & Assert
        assertThrows(NullPointerException.class,
                () -> controller.registerCountry("Portugal", null));
    }
}