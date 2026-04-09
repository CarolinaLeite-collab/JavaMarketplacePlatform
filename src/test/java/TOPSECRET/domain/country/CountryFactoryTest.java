package TOPSECRET.domain.country;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockConstruction;

class CountryFactoryTest {

    @Test
    void shouldSuccessfullyCreateCountry() {
        // Arrange
        String countryName = "Deutschland";

        // SUT
        CountryFactory factory = new CountryFactory();

        try (MockedConstruction<Country> mocked = mockConstruction(Country.class)) {
            // Act
            Country result = factory.createCountry(countryName);

            // Assert
            assertEquals(1, mocked.constructed().size());
            assertSame(mocked.constructed().get(0), result);
        }
    }
}
