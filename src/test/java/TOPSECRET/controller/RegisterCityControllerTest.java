package TOPSECRET.controller;

import TOPSECRET.domain.city.City;
import TOPSECRET.domain.city.CityFactory;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.repository.ICityRepo;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterCityControllerTest {

    private ICityRepo _iCityRepoDouble;
    private ICountryRepo _iCountryRepoDouble;
    private CityFactory _cityFactoryDouble;
    private Country _countryDouble;
    private City _cityDouble;
    private User _adminDouble;
    private User _nonAdminDouble;

    @BeforeEach
    void setUp() {
        _iCityRepoDouble = mock(ICityRepo.class);
        _iCountryRepoDouble = mock(ICountryRepo.class);
        _cityFactoryDouble = mock(CityFactory.class);
        _countryDouble = mock(Country.class);
        _cityDouble = mock(City.class);
        _adminDouble = mock(User.class);
        _nonAdminDouble = mock(User.class);

        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_nonAdminDouble.hasRole(Role.ADMIN)).thenReturn(false);
    }

    @Test
    void shouldConstructController() {
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        assertNotNull(controller);
    }

    @Test
    void constructorWithRepositoriesDoesNotThrow() {
        assertDoesNotThrow(() -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble));
    }

    @Test
    void shouldAllowAdminToCreateController() {
        assertDoesNotThrow(() -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble));
    }

    @Test
    void shouldRejectNonAdminUser() {
        assertThrows(SecurityException.class,
                () -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _nonAdminDouble));
    }

    @Test
    void registerCityCallsRepoAndReturnsCreatedCity() {
        CountryId countryId = new CountryId("PT");
        when(_iCountryRepoDouble.ofIdentity(countryId)).thenReturn(Optional.of(_countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);
        doReturn(_cityDouble).when(_iCityRepoDouble).addCity(any(City.class));

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        City result = controller.registerCity("Porto", countryId);

        assertSame(_cityDouble, result);
        verify(_iCountryRepoDouble).ofIdentity(countryId);
        verify(_cityFactoryDouble).createCity("Porto", _countryDouble);
        verify(_iCityRepoDouble).addCity(_cityDouble);
    }

    @Test
    void registerCityShouldThrowWhenCountryNotFound() {
        CountryId countryId = new CountryId("PT");
        when(_iCountryRepoDouble.ofIdentity(countryId)).thenReturn(Optional.empty());

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("Porto", countryId));
    }

    @Test
    void registerCityShouldThrowWhenCityAlreadyExists() {
        CountryId countryId = new CountryId("PT");
        when(_iCountryRepoDouble.ofIdentity(countryId)).thenReturn(Optional.of(_countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);
        when(_iCityRepoDouble.addCity(any(City.class)))
                .thenThrow(new IllegalStateException("City already exists for this country"));

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        assertThrows(IllegalStateException.class,
                () -> controller.registerCity("Porto", countryId));
    }

    @Test
    void registerCityWithCountryObjectDelegatesToCountryId() {
        CountryId countryId = new CountryId("PT");
        when(_countryDouble.identity()).thenReturn(countryId);
        when(_iCountryRepoDouble.ofIdentity(countryId)).thenReturn(Optional.of(_countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);
        doReturn(_cityDouble).when(_iCityRepoDouble).addCity(any(City.class));

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        City result = controller.registerCity("Porto", _countryDouble);

        assertSame(_cityDouble, result);
    }
}