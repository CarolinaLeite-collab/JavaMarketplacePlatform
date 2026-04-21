package MITELOVERS.controller;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.city.CityFactory;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.repository.ICityRepo;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.domain.valueobject.UserId;
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
    private UserId _adminIdDouble;

    @BeforeEach
    void setUp() {
        _iCityRepoDouble = mock(ICityRepo.class);
        _iCountryRepoDouble = mock(ICountryRepo.class);
        _cityFactoryDouble = mock(CityFactory.class);
        _countryIdDouble = mock(CountryId.class);
        _cityDouble = mock(City.class);
        _adminIdDouble = mock(UserId.class);
    }

    @Test
    void shouldConstructController() {
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        assertNotNull(controller);
    }

    @Test
    void constructorWithRepositoriesDoesNotThrow() {
        assertDoesNotThrow(() -> new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble));
    }

    @Test
    void registerCityCallsRepoAndReturnsCreatedCity() {
        Country countryDouble = mock(Country.class);
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble)).thenReturn(Optional.of(countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble)).thenReturn(_cityDouble);
        when(_iCityRepoDouble.addCity(any(City.class))).thenReturn(_cityDouble);

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        City result = controller.registerCity("Porto", _countryIdDouble);

        assertSame(_cityDouble, result);
        verify(_iCountryRepoDouble).ofIdentity(_countryIdDouble);
        verify(_cityFactoryDouble).createCity("Porto", _countryIdDouble);
        verify(_iCityRepoDouble).addCity(_cityDouble);
    }

    @Test
    void registerCityShouldThrowWhenCountryNotFound() {
        CountryId countryId = new CountryId("PT");
        when(_iCountryRepoDouble.ofIdentity(countryId)).thenReturn(Optional.empty());

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("Porto", countryId));
    }

    @Test
    void registerCityShouldThrowWhenCityAlreadyExists() {
        Country countryDouble = mock(Country.class);
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble)).thenReturn(Optional.of(countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble)).thenReturn(_cityDouble);
        when(_iCityRepoDouble.addCity(any(City.class)))
                .thenThrow(new IllegalStateException("City already exists for this country"));

        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        assertThrows(IllegalStateException.class,
                () -> controller.registerCity("Porto", _countryIdDouble));
    }
}
