package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterCityControllerTest {
    private ICityRepo _iCityRepoDouble;
    private ICountryRepo _iCountryRepoDouble;
    private Country _countryDouble;
    private City _cityDouble;
    private User _adminDouble;
    private User _nonAdminDouble;

    @BeforeEach
    void setUp() {
        _iCityRepoDouble = mock(ICityRepo.class);
        _iCountryRepoDouble = mock(ICountryRepo.class);
        _countryDouble = mock(Country.class);
        _cityDouble = mock(City.class);
        _adminDouble = mock(User.class);
        _nonAdminDouble = mock(User.class);

        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_nonAdminDouble.hasRole(Role.ADMIN)).thenReturn(false);
    }

    @Test
    void shouldConstructController() {
        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _adminDouble);
    }

    @Test
    void constructorWithRepositoriesDoesNotThrow() {
        //Act & Assert
        assertDoesNotThrow(() -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _adminDouble));
    }

    @Test
    void getCountriesReturnsAllCountriesFromRepository() {
        // Arrange
        List<Country> countries = List.of(_countryDouble);
        when(_iCountryRepoDouble.getAllCountries()).thenReturn(countries);

        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _adminDouble);

        // Act
        List<Country> result = controller.getAllCountries();

        // Assert
        assertEquals(1, result.size());
        assertSame(_countryDouble, result.get(0));
        verify(_iCountryRepoDouble, times(1)).getAllCountries();
    }

    @Test
    void registerCityCallsRepoAndReturnsCreatedCity() {
        // Arrange
        CountryId countryId = new CountryId("PT");
        when(_iCountryRepoDouble.ofIdentity(countryId)).thenReturn(Optional.of(_countryDouble));
        when(_iCityRepoDouble.registerCity("Porto", _countryDouble)).thenReturn(_cityDouble);

        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _adminDouble);

        // Act
        City result = controller.registerCity("Porto", countryId);

        // Assert
        assertSame(_cityDouble, result);
        InOrder inOrder = inOrder(_iCountryRepoDouble, _iCityRepoDouble);
        inOrder.verify(_iCountryRepoDouble).ofIdentity(countryId);
        inOrder.verify(_iCityRepoDouble).registerCity("Porto", _countryDouble);
    }

    @Test
    void registerCityWhenRepoThrowsExceptionPropagatesException() {
        // Arrange
        CountryId countryId = new CountryId("PT");
        when(_iCountryRepoDouble.ofIdentity(countryId)).thenReturn(Optional.of(_countryDouble));
        when(_iCityRepoDouble.registerCity("Porto", _countryDouble))
                .thenThrow(new IllegalStateException("City already exists for this country"));

        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _adminDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> controller.registerCity("Porto", countryId));
    }

    @Test
    void shouldAllowAdminToCreateController() {
        assertDoesNotThrow(() -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _adminDouble));
    }

    @Test
    void shouldRejectNonAdminUser() {
        assertThrows(SecurityException.class, () -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _nonAdminDouble));
    }
}
