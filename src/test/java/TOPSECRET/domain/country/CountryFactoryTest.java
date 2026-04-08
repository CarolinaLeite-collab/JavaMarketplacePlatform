package TOPSECRET.domain.country;

import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
            Country newCountry = factory.createCountry("DE", "Deutschland");
            //assert
            assertEquals(countryName, newCountry.getCountryName());

        }
    }

    @Test
    void createCountryWithValueObjectsCreatesCountry() {
        // Arrange
        CountryFactory factory = new CountryFactory();

        try (MockedConstruction<Country> mocked =
                     mockConstruction(Country.class,
                             (mock, context) -> {
                                 when(mock.getCountryName()).thenReturn("Deutschland");
                             })) {
            // SUT
            Country result = factory.createCountry(new CountryId("DE"), new CountryName("Deutschland"));

            // Assert
            assertNotNull(result);
            assertEquals("Deutschland", result.getCountryName());
        }


    }
}
