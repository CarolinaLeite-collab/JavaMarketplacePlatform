package TOPSECRET.domain.country;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

class CountryFactoryTest {

    @Test
    void shouldSuccessfullyCreateCountry() {
        // arrange
        String countryName = "Deutschland";

        //SUT
        CountryFactory factory = new CountryFactory();

        try (MockedConstruction<Country> mocked =
                     mockConstruction(Country.class,
                             (mock, context) -> {
                                 when(mock.getCountryName())
                                         .thenReturn("Deutschland");
                             })) {
            // act
            Country newCountry = factory.createCountry(countryName);
            //assert
            assertEquals(countryName, newCountry.getCountryName());

        }
    }


}
