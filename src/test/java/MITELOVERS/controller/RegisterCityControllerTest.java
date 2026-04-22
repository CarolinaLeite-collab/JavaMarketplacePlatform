package MITELOVERS.controller;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.city.CityFactory;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.repository.ICityRepo;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterCityControllerTest {

    private ICityRepo _iCityRepoDouble;
    private ICountryRepo _iCountryRepoDouble;
    private CityFactory _cityFactoryDouble;
    private CountryId _countryIdDouble;
    private CityId _cityIdDouble;
    private City _cityDouble;
    private UserId _adminIdDouble;


    @BeforeEach
    void setUp() {
        _iCityRepoDouble = mock(ICityRepo.class);
        _iCountryRepoDouble = mock(ICountryRepo.class);
        _cityFactoryDouble = mock(CityFactory.class);
        _countryIdDouble = mock(CountryId.class);
        _cityIdDouble = mock(CityId.class);
        _cityDouble = mock(City.class);
        _adminIdDouble = mock(UserId.class);

        when(_cityDouble.identity()).thenReturn(_cityIdDouble);
    }


    @Test
    void shouldConstructController() {
        new RegisterCityController(
                _iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);
    }


    @Test
    void constructorWithRepositoriesDoesNotThrow() {
        assertDoesNotThrow(() -> new RegisterCityController(
                _iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble));
    }


    @Test
    void registerCityShouldCreateAndReturnCity() {
        // Arrange
        Country countryDouble = mock(Country.class);
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.of(countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble))
                .thenReturn(_cityDouble);
        when(_iCityRepoDouble.containsOfIdentity(_cityIdDouble))
                .thenReturn(false);
        when(_iCityRepoDouble.save(_cityDouble))
                .thenReturn(_cityDouble);

        RegisterCityController controller = new RegisterCityController(
                _iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        // Act
        City result = controller.registerCity("Porto", _countryIdDouble);

        // Assert
        assertSame(_cityDouble, result);
        verify(_cityFactoryDouble).createCity("Porto", _countryIdDouble);
        verify(_iCityRepoDouble).save(_cityDouble);
    }


    @Test
    void registerCityShouldCallSaveOnRepository() {
        // Arrange
        Country countryDouble = mock(Country.class);
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.of(countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble))
                .thenReturn(_cityDouble);
        when(_iCityRepoDouble.containsOfIdentity(_cityIdDouble))
                .thenReturn(false);

        RegisterCityController controller = new RegisterCityController(
                _iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        // Act
        controller.registerCity("Porto", _countryIdDouble);

        // Assert
        verify(_iCityRepoDouble).save(_cityDouble);
    }


    @Test
    void registerCityShouldThrowWhenCountryNotFound() {
        // Arrange
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.empty());

        RegisterCityController controller = new RegisterCityController(
                _iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("Porto", _countryIdDouble));
    }


    @Test
    void registerCityCountryNotFoundShouldNeverCallFactory() {
        // Arrange
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.empty());

        RegisterCityController controller = new RegisterCityController(
                _iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        // Act
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerCity("Porto", _countryIdDouble));

        // Assert
        verify(_cityFactoryDouble, never()).createCity(any(), any());
    }


    @Test
    void registerCityShouldThrowWhenCityAlreadyExists() {
        // Arrange
        Country countryDouble = mock(Country.class);
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.of(countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble))
                .thenReturn(_cityDouble);
        when(_iCityRepoDouble.containsOfIdentity(_cityIdDouble))
                .thenReturn(true); // já existe

        RegisterCityController controller = new RegisterCityController(
                _iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.registerCity("Porto", _countryIdDouble));
    }


    @Test
    void registerCityShouldThrowCorrectMessageWhenCityAlreadyExists() {
        // Arrange
        Country countryDouble = mock(Country.class);
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.of(countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble))
                .thenReturn(_cityDouble);
        when(_iCityRepoDouble.containsOfIdentity(_cityIdDouble))
                .thenReturn(true);

        RegisterCityController controller = new RegisterCityController(
                _iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> controller.registerCity("Porto", _countryIdDouble));

        // Assert
        assertEquals("City already exists for this country", ex.getMessage());
    }


    @Test
    void registerCityDuplicateShouldNeverCallSave() {
        // Arrange
        Country countryDouble = mock(Country.class);
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.of(countryDouble));
        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble))
                .thenReturn(_cityDouble);
        when(_iCityRepoDouble.containsOfIdentity(_cityIdDouble))
                .thenReturn(true);

        RegisterCityController controller = new RegisterCityController(
                _iCityRepoDouble, _iCountryRepoDouble, _cityFactoryDouble, _adminIdDouble);

        // Act
        assertThrows(IllegalStateException.class,
                () -> controller.registerCity("Porto", _countryIdDouble));

        // Assert
        verify(_iCityRepoDouble, never()).save(any());
    }

}