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
        //Act
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_countryRepo);
        //Assert
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
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_countryRepo);
        //Act
        Country country = controller.registerCountry("Portugal", user);
        // Assert
        assertNotNull(country);
    }

    @Test
    void shouldNotRegisterCountrySuccessfullyIfUserNotAdmin(){
        //Arrange
        User user = mock(User.class);
        when(user.hasRole(Role.ADMIN)).thenReturn(false);
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_countryRepo);
        //Act & Assert
        Assertions.assertThrows(SecurityException.class, () -> controller.registerCountry("Portugal", user));
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