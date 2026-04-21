package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountryIdTest {

    @Test
    void generatesIsoCodeFromCountryName() {
        // Arrange
        CountryName countryNameDouble = mock(CountryName.class);
        when(countryNameDouble.toString()).thenReturn("Portugal");

        // SUT
        CountryId pt = new CountryId(countryNameDouble);

        // Act
        String result = pt.toString();

        // Assert
        assertEquals("PT", result);
    }


    @Test
    void notEqualWhenDifferentCountryName() {
        // Arrange
        CountryName portugalDouble = mock(CountryName.class);
        when(portugalDouble.toString()).thenReturn("Portugal");
        CountryName spainDouble = mock(CountryName.class);
        when(spainDouble.toString()).thenReturn("Spain");

        // Act & SUT
        CountryId countryId = new CountryId(portugalDouble);
        CountryId countryId1 = new CountryId(spainDouble);

        // Assert
        assertNotEquals(countryId, countryId1);
    }

    @Test
    void isNotEqualToDifferentType() {
        // Arrange
        CountryName countryNameDouble = mock(CountryName.class);
        when(countryNameDouble.toString()).thenReturn("Portugal");

        // SUT
        CountryId countyId = new CountryId(countryNameDouble);

        // Act & Assert
        assertNotEquals(countyId, new Object());
    }

    @Test
    void isEqualToSameInstanceAndEquivalentNormalizedValue() {
        // Arrange
        CountryName countryNameDouble1 = mock(CountryName.class);
        when(countryNameDouble1.toString()).thenReturn("Portugal");
        CountryName countryNameDouble2 = mock(CountryName.class);
        when(countryNameDouble2.toString()).thenReturn("Portugal");

        // Act
        CountryId countryId = new CountryId(countryNameDouble1);
        CountryId countryId1 = new CountryId(countryNameDouble2);

        // Assert
        assertEquals(countryId, countryId1);
        assertEquals(countryId.hashCode(), countryId1.hashCode());
    }

    @Test
    void hashCodeMatchesIsoCode() {
        // Arrange
        CountryName countryNameDouble = mock(CountryName.class);
        when(countryNameDouble.toString()).thenReturn("Portugal");

        // Act & SUT
        CountryId countryId = new CountryId(countryNameDouble);

        // Assert
        assertEquals("PT".hashCode(), countryId.hashCode());
    }

    @Test
    void rejectsNonExistingCountryName() {
        // Arrange
        CountryName countryNameDouble = mock(CountryName.class);
        when(countryNameDouble.toString()).thenReturn("Atlantis");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CountryId(countryNameDouble)); // SUT

        // Assert
        assertTrue(ex.getMessage().contains("No ISO 3166 code found"));
    }

    @Test
    void acceptsValidIsoCodeWithTrimAndUppercaseNormalization() {
        // Arrange
        String isoCode = "  pt  ";

        // Act
        CountryId sut = new CountryId(isoCode); // SUT

        // Assert
        assertEquals("PT", sut.toString());
    }

    @Test
    void rejectsInvalidIsoCode() {
        // Arrange
        String invalidCode = "A1";

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CountryId(invalidCode)); // SUT

        // Assert
        assertTrue(ex.getMessage().contains("Invalid ISO 3166 code"));
    }

    @Test
    void rejectsNullIsoCode() {
        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CountryId((String) null));

        // Assert
        assertTrue(ex.getMessage().contains("null or blank"));
    }

    @Test
    void rejectsBlankIsoCode() {
        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CountryId("   "));

        // Assert
        assertTrue(ex.getMessage().contains("null or blank"));
    }

    @Test
    void rejectsNullCountryName() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new CountryId((CountryName) null));
    }

}
