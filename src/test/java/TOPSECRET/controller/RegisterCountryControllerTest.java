package TOPSECRET.controller;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterCountryControllerTest {

    private ICountryRepo _iCountryRepoDouble;
    private User _userDouble;
    private Country _countryDouble;
    private UserId _userIdDouble;

    @BeforeEach
    void setUp() {
        // Arrange
        _iCountryRepoDouble = mock(ICountryRepo.class);
        _userDouble = mock(User.class);
        _countryDouble = mock(Country.class);
        _userIdDouble = mock(UserId.class);
    }

    @Test
    void constructsControllerSuccessfully() {
        // Act & SUT
        new RegisterCountryController(_iCountryRepoDouble, _userIdDouble);
    }

    @Test
    void shouldRegisterCountrySuccessfully() {
        // Arrange
        String countryName = "Portugal";
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iCountryRepoDouble.addCountry(countryName)).thenReturn(_countryDouble);

        // SUT
        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _userIdDouble);

        // Act
        Country result = controller.registerCountry(_userDouble, countryName);

        // Assert
        assertSame(_countryDouble, result);
        verify(_iCountryRepoDouble, times(1)).addCountry(countryName);
    }

    @Test
    void shouldNotRegisterCountrySuccessfullyIfUserNotAdmin() {
        // Arrange
        String countryName = "Portugal";
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(SecurityException.class,
                () -> controller.registerCountry(_userDouble, countryName));

        // Assert
        verify(_iCountryRepoDouble, never()).addCountry(anyString());
    }
}