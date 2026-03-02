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
            Country newCountry = factory.create(countryName);
            //assert
            assertEquals(countryName, newCountry.getCountryName());
        }
    }

    @Test
    void shouldThrowExceptionWhenCreateCountry() {
        // arrange
        String countryName = "hgfdfjygfgvjh";
        try (MockedConstruction<Country> mocked =
                     mockConstruction(Country.class, (mock, context) -> {
                         throw new RuntimeException("dfrdfgtfrghfvhbgvbh");
                     })) {
            //SUT
            CountryFactory factory = new CountryFactory();

            // act and assert
            assertThrows(InstantiationException.class, () -> factory.create(countryName));
        }
    }
}
