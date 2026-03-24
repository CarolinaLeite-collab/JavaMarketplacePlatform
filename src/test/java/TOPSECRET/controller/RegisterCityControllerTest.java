package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

class RegisterCityControllerTest {

    private ICityRepo _iCityRepoDouble;
    private ICountryRepo _countryRepoDouble;
    private Country _countryDouble;
    private City _cityDouble;
    private User _adminDouble;

    @BeforeEach
    void setUp() {
        _iCityRepoDouble = mock(ICityRepo.class);
        _countryRepoDouble = mock(ICountryRepo.class);
        _countryDouble = mock(Country.class);
        _cityDouble = mock(City.class);
        _adminDouble = mock(User.class);
    }

    @Test
    void shouldConstructController() {
        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _countryRepoDouble, _adminDouble);
    }

    @Test
    void constructorWithRepositoriesDoesNotThrow() {
        //Act & Assert
        assertDoesNotThrow(() -> new RegisterCityController(_iCityRepoDouble, _countryRepoDouble, _adminDouble));
    }

    @Test
    void getCountriesReturnsAllCountriesFromRepository() {
        // Arrange
        List<Country> countries = List.of(_countryDouble);
        when(_countryRepoDouble.getAllCountries()).thenReturn(countries);

        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _countryRepoDouble, _adminDouble);

        // Act
        List<Country> result = controller.getAllCountries();

        // Assert
        assertEquals(1, result.size());
        assertSame(_countryDouble, result.get(0));
        verify(_countryRepoDouble, times(1)).getAllCountries();
    }

    @Test
    void registerCityCallsRepoAndReturnsCreatedCity() {
        // Arrange
        when(_iCityRepoDouble.registerCity("Porto", _countryDouble)).thenReturn(_cityDouble);

        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _countryRepoDouble, _adminDouble);

        // Act
        City result = controller.registerCity("Porto", _countryDouble);

        // Assert
        assertSame(_cityDouble, result);
        verify(_iCityRepoDouble, times(1)).registerCity("Porto", _countryDouble);
    }

    @Test
    void registerCityWhenRepoThrowsExceptionPropagatesException() {
        // Arrange
        when(_iCityRepoDouble.registerCity("Porto", _countryDouble))
                .thenThrow(new IllegalStateException("City already exists for this country"));

        //SUT
        RegisterCityController controller = new RegisterCityController(_iCityRepoDouble, _countryRepoDouble, _adminDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> controller.registerCity("Porto", _countryDouble));
    }
}
