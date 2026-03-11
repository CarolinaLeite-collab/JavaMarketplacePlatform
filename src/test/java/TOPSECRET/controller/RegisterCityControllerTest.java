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

    private CityRepo cityRepo;
    private CountryRepo countryRepo;
    private Country country;
    private City city;

    @BeforeEach
    void setUp() {
        cityRepo = mock(CityRepo.class);
        countryRepo = mock(CountryRepo.class);
        country = mock(Country.class);
        city = mock(City.class);
    }

    @Test
    void constructorWithRepositoriesDoesNotThrow() {
        assertDoesNotThrow(() -> new RegisterCityController(cityRepo, countryRepo));
    }

    @Test
    void getCountriesReturnsCountriesFromRepository() {
        // Arrange
        List<Country> countries = List.of(country);
        when(countryRepo.getAllCountries()).thenReturn(countries);

        //SUT
        RegisterCityController controller = new RegisterCityController(cityRepo, countryRepo);

        // Act
        List<Country> result = controller.getCountries();

        // Assert
        assertEquals(1, result.size());
        assertSame(country, result.get(0));
        verify(countryRepo, times(1)).getAllCountries();
    }

    @Test
    void registerCityCallsRepoAndReturnsCreatedCity() {
        // Arrange
        when(cityRepo.add("Porto", country)).thenReturn(city);

        //SUT
        RegisterCityController controller = new RegisterCityController(cityRepo, countryRepo);

        // Act
        City result = controller.registerCity("Porto", country);

        // Assert
        assertSame(city, result);
        verify(cityRepo, times(1)).add("Porto", country);
    }

    @Test
    void registerCityWhenRepoThrowsExceptionPropagatesException() {
        // Arrange
        when(cityRepo.add("Porto", country))
                .thenThrow(new IllegalStateException("City already exists for this country"));

        //SUT
        RegisterCityController controller = new RegisterCityController(cityRepo, countryRepo);

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> controller.registerCity("Porto", country)
        );
    }
}
