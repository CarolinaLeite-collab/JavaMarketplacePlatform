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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterCityControllerTest {

    private ICityRepo _iCityRepoDouble;
    private ICountryRepo _iCountryRepoDouble;
    private CityFactory _cityFactoryDouble;
    private CountryId _countryIdDouble;
    private City _cityDouble;
    private User _adminDouble;
    private User _nonAdminDouble;

    @BeforeEach
    void setUp() {
        _iCityRepoDouble = mock(ICityRepo.class);
        _iCountryRepoDouble = mock(ICountryRepo.class);
        _cityFactoryDouble = mock(CityFactory.class);
        _countryIdDouble = mock(CountryId.class);
        _cityDouble = mock(City.class);
        _adminDouble = mock(User.class);
        _nonAdminDouble = mock(User.class);

        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_nonAdminDouble.hasRole(Role.ADMIN)).thenReturn(false);
    }

    @Test
    void shouldConstructController() {
        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        //Assert
        assertNotNull(controller);
    }

    @Test
    void constructorWithRepositoriesDoesNotThrow() {
        //Act & Assert
        assertDoesNotThrow(() -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble));
    }

    @Test
    void shouldAllowAdminToCreateController() {
        //Act & Assert
        assertDoesNotThrow(() -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble));
    }

    @Test
    void shouldRejectNonAdminUser() {
        //Act & Assert
        assertThrows(SecurityException.class,
                () -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _nonAdminDouble));
    }

    @Test
    void registerCityCallsRepoAndReturnsCreatedCity() {
        //Arrange
        Country countryDoble = mock(Country.class);
        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble)).thenReturn(_cityDouble);
        when(_iCityRepoDouble.addCity(any(City.class))).thenReturn(_cityDouble);
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble)).thenReturn(Optional.of(countryDoble));

        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        //Act
        City result = controller.registerCity("Porto", _countryIdDouble);

        //Assert
        assertSame(_cityDouble, result);
        verify(_iCountryRepoDouble).ofIdentity(_countryIdDouble);
        verify(_cityFactoryDouble).createCity("Porto", _countryIdDouble);
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
        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble)).thenReturn(_cityDouble);
        when(_iCityRepoDouble.addCity(any(City.class)))
                .thenThrow(new IllegalStateException("City already exists for this country"));

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminDouble);

        assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("Porto", _countryIdDouble));
    }

}