package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

class CountryFactoryTest {

    @Test
    void shouldSuccessfullyCreateCountry() throws InstantiationException {
        // arrange
        String countryName = "Deutschland";
        try (MockedConstruction<Country> mocked =
                     mockConstruction(Country.class,
                             (mock, context) -> {
                                 when(mock.getCountryName())
                                         .thenReturn("Deutschland");
                             })) {
            CountryFactory factory = new CountryFactory();
            // act
            Country newCountry = factory.createFactory(countryName);
            //assert
            assertEquals(countryName, newCountry.getCountryName());
        }
    }


}
