package TOPSECRET.controller;

import TOPSECRET.domain.Country;
import TOPSECRET.domain.ICountryRepo;
import TOPSECRET.domain.Role;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterCountryControllerTest {
    private ICountryRepo _iCountryRepoDouble;
    private User _adminDouble;

    @BeforeEach
    void setUp(){
        _iCountryRepoDouble = mock(ICountryRepo.class);
        _adminDouble = mock(User.class);
    }

    @Test
    void constructsControllerSuccessfully() {
        //Act
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _adminDouble);
    }

    @Test
    void shouldRegisterCountrySuccessfully() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_iCountryRepoDouble.registerCountry("Portugal")).thenReturn(portugal);

        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _adminDouble);
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
        SecurityException exception = assertThrows(SecurityException.class, () -> new RegisterCountryController(_iCountryRepoDouble, _adminDouble));

        //Assert
        assertEquals("User is not authorized to register countries", exception.getMessage());
    }
}

