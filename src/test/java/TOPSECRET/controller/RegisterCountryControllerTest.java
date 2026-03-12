package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterCountryControllerTest {
    private CountryRepo _countryRepoDouble;
    private User _adminDouble;

    @BeforeEach
    void setUp(){
        _countryRepoDouble = mock(CountryRepo.class);
        _adminDouble = mock(User.class);
    }

    @Test
    void constructsControllerSuccessfully() {
        //Act
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_countryRepoDouble, _adminDouble);
    }

    @Test
    void shouldRegisterCountrySuccessfully() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_countryRepoDouble.registerCountry("Portugal")).thenReturn(portugal);

        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_countryRepoDouble, _adminDouble);
        //Act
        Country country = controller.registerCountry("Portugal");
        // Assert
        assertNotNull(country);
    }

    @Test
    void shouldNotRegisterCountrySuccessfullyIfUserNotAdmin() {
        //Arrange
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        //Act
        SecurityException exception = assertThrows(SecurityException.class, () -> new RegisterCountryController(_countryRepoDouble, _adminDouble));

        //Assert
        assertEquals("User is not authorized to register countries", exception.getMessage());
    }
}
