package MITELOVERS.controller;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.city.CityFactory;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.repository.ICityRepo;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(RegisterCityController.class)
@ActiveProfiles("jpa")
class RegisterCityControllerTest {

    @MockBean
    private ICityRepo _iCityRepoDouble;

    @MockBean
    private ICountryRepo _iCountryRepoDouble;

    @MockBean
    private CityFactory _cityFactoryDouble;

    @Autowired
    private RegisterCityController _controller;

    private CountryId _countryIdDouble;
    private CityId _cityIdDouble;
    private City _cityDouble;

    @BeforeEach
    void setUp() {
        _countryIdDouble = mock(CountryId.class);
        _cityIdDouble = mock(CityId.class);
        _cityDouble = mock(City.class);

        when(_cityDouble.identity()).thenReturn(_cityIdDouble);
    }

    @Test
    void shouldConstructController() {
        assertNotNull(_controller);
    }

    @Test
    void registerCityShouldCreateAndReturnCity() {
        Country countryDouble = mock(Country.class);

        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.of(countryDouble));

        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble))
                .thenReturn(_cityDouble);

        when(_iCityRepoDouble.containsOfIdentity(_cityIdDouble))
                .thenReturn(false);

        when(_iCityRepoDouble.save(_cityDouble))
                .thenReturn(_cityDouble);

        City result = _controller.registerCity("Porto", _countryIdDouble);

        assertSame(_cityDouble, result);
        verify(_cityFactoryDouble).createCity("Porto", _countryIdDouble);
        verify(_iCityRepoDouble).save(_cityDouble);
    }

    @Test
    void registerCityShouldCallSaveOnRepository() {
        Country countryDouble = mock(Country.class);

        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.of(countryDouble));

        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble))
                .thenReturn(_cityDouble);

        when(_iCityRepoDouble.containsOfIdentity(_cityIdDouble))
                .thenReturn(false);

        _controller.registerCity("Porto", _countryIdDouble);

        verify(_iCityRepoDouble).save(_cityDouble);
    }

    @Test
    void registerCityShouldThrowWhenCountryNotFound() {
        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> _controller.registerCity("Porto", _countryIdDouble));

        verify(_cityFactoryDouble, never()).createCity(any(), any());
    }

    @Test
    void registerCityShouldThrowWhenCityAlreadyExists() {
        Country countryDouble = mock(Country.class);

        when(_iCountryRepoDouble.ofIdentity(_countryIdDouble))
                .thenReturn(Optional.of(countryDouble));

        when(_cityFactoryDouble.createCity("Porto", _countryIdDouble))
                .thenReturn(_cityDouble);

        when(_iCityRepoDouble.containsOfIdentity(_cityIdDouble))
                .thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> _controller.registerCity("Porto", _countryIdDouble));

        assertEquals("City already exists for this country", ex.getMessage());

        verify(_iCityRepoDouble, never()).save(any());
    }

}