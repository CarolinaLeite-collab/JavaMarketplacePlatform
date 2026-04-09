package TOPSECRET.controller;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterCountryControllerTest {

    private AutoCloseable _mocks;

    @Mock
    private ICountryRepo _iCountryRepoDouble;
    @Mock
    private User _userDouble;
    @Mock
    private Country _countryDouble;

    @InjectMocks
    // SUT
    private RegisterCountryController _controller;

    @BeforeEach
    void setUp() {
        _mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        _mocks.close();
    }

    @Test
    void constructsControllerSuccessfully() {
        assertNotNull(_controller);
    }

    @Test
    void shouldRegisterCountrySuccessfully() {
        // Arrange
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iCountryRepoDouble.addCountry("Portugal")).thenReturn(_countryDouble);

        // Act
        Country result = _controller.registerCountry(_userDouble, "Portugal");

        // Assert
        assertSame(_countryDouble, result);
        verify(_iCountryRepoDouble, times(1)).addCountry("Portugal");
    }

    @Test
    void shouldNotRegisterCountrySuccessfullyIfUserNotAdmin() {
        // Arrange
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // Act / Assert
        SecurityException exception = assertThrows(SecurityException.class,
                () -> _controller.registerCountry(_userDouble, "Portugal"));

        // Assert
        assertEquals("User is not authorized to register countries", exception.getMessage());
        verify(_iCountryRepoDouble, never()).addCountry(anyString());
    }
}

