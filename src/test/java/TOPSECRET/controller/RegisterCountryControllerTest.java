package TOPSECRET.controller;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.country.CountryFactory;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterCountryControllerTest {

    private AutoCloseable _mocks;

    @Mock
    private ICountryRepo _iCountryRepoDouble;
    @Mock
    private CountryFactory _countryFactory;
    @Mock
    private User _adminDouble;

    @InjectMocks
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
        Country created = org.mockito.Mockito.mock(Country.class);
        Country saved = org.mockito.Mockito.mock(Country.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iCountryRepoDouble.containsOfIdentity(any(CountryId.class))).thenReturn(false);
        when(_countryFactory.createCountry(any(CountryId.class), any(CountryName.class))).thenReturn(created);
        when(_iCountryRepoDouble.save(created)).thenReturn(saved);

        // Act
        Optional<Country> opt = _controller.registerCountry(_adminDouble, "PT", "Portugal");

        // Assert
        assertTrue(opt.isPresent());
        assertSame(saved, opt.orElseThrow());
        verify(_countryFactory).createCountry(any(CountryId.class), any(CountryName.class));
        verify(_iCountryRepoDouble).save(created);
    }

    @Test
    void shouldNotRegisterCountrySuccessfullyIfUserNotAdmin() {
        // Arrange
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // Act
        SecurityException exception = assertThrows(SecurityException.class,
                () -> _controller.registerCountry(_adminDouble, null, null));

        // Assert
        assertEquals("User is not authorized to register countries", exception.getMessage());
        verify(_iCountryRepoDouble, never()).containsOfIdentity(any(CountryId.class));
        verify(_countryFactory, never()).createCountry(any(CountryId.class), any(CountryName.class));
        verify(_iCountryRepoDouble, never()).save(any(Country.class));
    }

    @Test
    void shouldReturnExistingCountryWhenCountryAlreadyExists() {
        // Arrange
        Country existing = org.mockito.Mockito.mock(Country.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iCountryRepoDouble.containsOfIdentity(any(CountryId.class))).thenReturn(true);
        when(_iCountryRepoDouble.ofIdentity(any(CountryId.class))).thenReturn(Optional.of(existing));

        // Act
        Optional<Country> opt = _controller.registerCountry(_adminDouble, "PT", "Portugal");

        // Assert
        assertTrue(opt.isPresent());
        assertSame(existing, opt.orElseThrow());
        verify(_iCountryRepoDouble, never()).save(any(Country.class));
        verify(_countryFactory, never()).createCountry(any(CountryId.class), any(CountryName.class));
    }

    @Test
    void shouldReturnEmptyOptionalWhenSaveReturnsNull() {
        // Arrange
        Country created = org.mockito.Mockito.mock(Country.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_iCountryRepoDouble.containsOfIdentity(any(CountryId.class))).thenReturn(false);
        when(_countryFactory.createCountry(any(CountryId.class), any(CountryName.class))).thenReturn(created);
        when(_iCountryRepoDouble.save(created)).thenReturn(null);

        // Act
        Optional<Country> opt = _controller.registerCountry(_adminDouble, "PT", "Portugal");

        // Assert
        assertTrue(opt.isEmpty());
    }
}

