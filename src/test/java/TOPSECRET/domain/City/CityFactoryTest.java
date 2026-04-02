package TOPSECRET.domain.City;

import TOPSECRET.domain.country.Country;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityFactoryTest {

    @Test
    void shouldSuccessfullyCreateCity() {
        // arrange
        String cityName = "Porto";
        Country countryDouble = mock(Country.class);

        //SUT
        CityFactory factory = new CityFactory();

        try (MockedConstruction<City> mocked =
                     mockConstruction(City.class,
                             (mock, context) -> {
                                 when(mock.getName())
                                         .thenReturn("Porto");
                             })) {
            // act
            City newCity = factory.createCity(cityName, countryDouble);
            //assert
            assertEquals(cityName, newCity.getName());
        }
    }

}