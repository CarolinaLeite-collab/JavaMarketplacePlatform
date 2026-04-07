package TOPSECRET.controller;

import TOPSECRET.domain.city.City;
import TOPSECRET.domain.city.CityFactory;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.repository.ICityRepo;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.CityId;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void getCountriesReturnsAllCountriesFromRepository() {
        List<Country> countries = List.of(_countryDouble);
        when(_iCountryRepoDouble.getAllCountries()).thenReturn(countries);

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        List<Country> result = controller.getAllCountries();

        assertEquals(1, result.size());
        assertSame(_countryDouble, result.get(0));
        verify(_iCountryRepoDouble, times(1)).getAllCountries();
    }

    @Test
    void registerCityCallsRepoAndReturnsCreatedCity() {
        CountryId countryId = new CountryId("PT");
        when(_iCountryRepoDouble.ofIdentity(countryId)).thenReturn(Optional.of(_countryDouble));
        when(_countryDouble.identity()).thenReturn(countryId);
        when(_iCityRepoDouble.containsOfIdentity(any(CityId.class))).thenReturn(false);
        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);
        doReturn(_cityDouble).when(_iCityRepoDouble).save(any(City.class));

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        City result = controller.registerCity("Porto", countryId);

        assertSame(_cityDouble, result);
        verify(_iCountryRepoDouble).ofIdentity(countryId);
        verify(_iCityRepoDouble).containsOfIdentity(any(CityId.class));
        verify(_cityFactoryDouble).createCity("Porto", _countryDouble);
        verify(_iCityRepoDouble).save(_cityDouble);
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
        when(_countryDouble.identity()).thenReturn(countryId);
        when(_iCityRepoDouble.containsOfIdentity(any(CityId.class))).thenReturn(true);

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        assertThrows(IllegalStateException.class,
                () -> controller.registerCity("Porto", countryId));
    }

    @Test
    void registerCityWithCountryObjectDelegatesToCountryId() {
        CountryId countryId = new CountryId("PT");
        when(_countryDouble.identity()).thenReturn(countryId);
        when(_iCountryRepoDouble.ofIdentity(countryId)).thenReturn(Optional.of(_countryDouble));
        when(_iCityRepoDouble.containsOfIdentity(any(CityId.class))).thenReturn(false);
        when(_cityFactoryDouble.createCity("Porto", _countryDouble)).thenReturn(_cityDouble);
        doReturn(_cityDouble).when(_iCityRepoDouble).save(any(City.class));

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        City result = controller.registerCity("Porto", _countryDouble);

        assertSame(_cityDouble, result);
    }
}