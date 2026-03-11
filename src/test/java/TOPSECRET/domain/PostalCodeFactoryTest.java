package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostalCodeFactoryTest {

    @Test
    void shouldCreatePortuguesePostalCode() {
        // Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");

        try (MockedConstruction<PortuguesePostalCode> mocked =
                     mockConstruction(PortuguesePostalCode.class)) {

            PostalCodeFactory factory = new PostalCodeFactory();

            // Act
            PostalCode postalCode = factory.createPostalCode(country, "3720-748");

            // Assert
            assertEquals(1, mocked.constructed().size());
            assertTrue(postalCode instanceof PortuguesePostalCode);
        }
    }

    @Test
    void shouldCreateUSZipCode() {
        // Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("UNITED STATES");

        try (MockedConstruction<USZipCode> mocked =
                     mockConstruction(USZipCode.class)) {

            PostalCodeFactory factory = new PostalCodeFactory();

            // Act
            PostalCode postalCode = factory.createPostalCode(country, "12345");

            // Assert
            assertEquals(1, mocked.constructed().size());
            assertTrue(postalCode instanceof USZipCode);
        }
    }

    @Test
    void shouldCreateFiveDigitPostalCodeForSupportedCountry() {
        // Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("GERMANY");

        try (MockedConstruction<FiveDigitPostalCode> mocked =
                     mockConstruction(FiveDigitPostalCode.class)) {

            PostalCodeFactory factory = new PostalCodeFactory();

            // Act
            PostalCode postalCode = factory.createPostalCode(country, "12345");

            // Assert
            assertEquals(1, mocked.constructed().size());
            assertTrue(postalCode instanceof FiveDigitPostalCode);
        }
    }

    @Test
    void shouldCreateGenericPostalCodeForOtherCountries() {
        // Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("BRAZIL");

        try (MockedConstruction<GenericPostalCode> mocked =
                     mockConstruction(GenericPostalCode.class)) {

            PostalCodeFactory factory = new PostalCodeFactory();

            // Act
            PostalCode postalCode = factory.createPostalCode(country, "ABC123");

            // Assert
            assertEquals(1, mocked.constructed().size());
            assertTrue(postalCode instanceof GenericPostalCode);
        }
    }
}