package MITELOVERS.domain.country;

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
        CountryFactory factory = new CountryFactory(); // SUT
        Country result;
        int constructedCount;
        Country mockedInstance;

        // Act
        try (MockedConstruction<Country> mocked = mockConstruction(Country.class)) {
            result = factory.createCountry(countryName);

            constructedCount = mocked.constructed().size();
            mockedInstance = mocked.constructed().get(0);
        }

        // Assert
        assertEquals(1, constructedCount);
        assertSame(mockedInstance, result);
    }
}
