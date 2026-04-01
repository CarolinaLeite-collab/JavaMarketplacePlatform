package TOPSECRET.controller;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.country.CountryFactory;
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
    private CountryFactory _countryFactory;

    @BeforeEach
    void setUp(){
        _iCountryRepoDouble = mock(ICountryRepo.class);
        _adminDouble = mock(User.class);
        _countryFactory = new CountryFactory();
    }

    @Test
    void constructsControllerSuccessfully() {
        //Act
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _countryFactory);
    }

    @Test
    void shouldRegisterCountrySuccessfully() throws InstantiationException {
        //Arrange
        Country portugal = mock(Country.class);
        when(_iCountryRepoDouble.save(org.mockito.ArgumentMatchers.any())).thenReturn(portugal);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        //SUT
        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _countryFactory);
        //Act
        java.util.Optional<Country> opt = controller.registerCountry(_adminDouble, "PT", "Portugal");
        // Assert
        assertTrue(opt.isPresent());
        assertSame(portugal, opt.get());
        org.mockito.Mockito.verify(_iCountryRepoDouble).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldNotRegisterCountrySuccessfullyIfUserNotAdmin() {
        //Arrange
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(false);
        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _countryFactory);
        //Act
        SecurityException exception = assertThrows(SecurityException.class, () -> controller.registerCountry(_adminDouble, null, "Portugal"));

        //Assert
        assertEquals("User is not authorized to register countries", exception.getMessage());
    }
}

