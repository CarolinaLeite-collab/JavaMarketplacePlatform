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
    private RegisterCityController controller;

    private Country country;
    private City city;

    @BeforeEach
    void setUp() {
        cityRepo = mock(CityRepo.class);
        countryRepo = mock(CountryRepo.class);

        controller = new RegisterCityController(cityRepo, countryRepo);

        country = mock(Country.class);
        city = mock(City.class);
    }

    @Test
    void constructor_withRepositories_doesNotThrow() {
        assertDoesNotThrow(() -> new RegisterCityController(cityRepo, countryRepo));
    }

    @Test
    void getCountries_returnsCountriesFromRepository() {
        // Arrange
        List<Country> countries = List.of(country);
        when(countryRepo.getAllCountries()).thenReturn(countries);

        // Act
        List<Country> result = controller.getCountries();

        // Assert
        assertEquals(1, result.size());
        assertSame(country, result.get(0));
        verify(countryRepo, times(1)).getAllCountries();
    }

    @Test
    void registerCity_callsRepoAndReturnsCreatedCity() {
        // Arrange
        when(cityRepo.add("Porto", country)).thenReturn(city);

        // Act
        City result = controller.registerCity("Porto", country);

        // Assert
        assertSame(city, result);
        verify(cityRepo, times(1)).add("Porto", country);
    }

    @Test
    void registerCity_whenRepoThrowsException_propagatesException() {
        // Arrange
        when(cityRepo.add("Porto", country))
                .thenThrow(new IllegalStateException("City already exists for this country"));

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> controller.registerCity("Porto", country)
        );
    }
}
