package MITELOVERS.domain.city;

import MITELOVERS.domain.valueobject.CountryId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityFactoryTest {

    @Test
    void shouldSuccessfullyCreateCity() {
        //Arrange
        String cityName = "Porto";
        CountryId countryIdDouble = mock(CountryId.class);

        //SUT
        CityFactory factory = new CityFactory();

        try (MockedConstruction<City> mocked =
                     mockConstruction(City.class,
                             (mock, context) -> {
                                 when(mock.getName())
                                         .thenReturn("Porto");
                             })) {
            //Act
            City newCity = factory.createCity(cityName, countryIdDouble);
            //assert
            assertEquals(cityName, newCity.getName());
        }
    }

}
