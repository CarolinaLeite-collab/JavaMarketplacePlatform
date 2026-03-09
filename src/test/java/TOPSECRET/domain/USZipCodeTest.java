package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class USZipCodeTest {

    @Test
    void shouldInstantiateUSZipCodeWithValidData(){
        //Arrange
        String postalCode = "10001";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("UNITED STATES");
        String expected = "UNITED STATES";
        //Act
        USZipCode zip = new USZipCode(country, postalCode);
        String result = zip.getPostalCodeCountry().getCountryName();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldInstantiateUSZipCodeWithUSCountryCode(){
        //Arrange
        String postalCode = "10001";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("US");
        String expected = "10001";
        //Act
        USZipCode zip = new USZipCode(country, postalCode);
        String result = zip.getValue();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldInstantiateUSZipCodeWithUSACountryCode(){
        //Arrange
        String postalCode = "10001";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("USA");
        String expected = "10001";
        //Act
        USZipCode zip = new USZipCode(country, postalCode);
        String result = zip.getValue();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNormalizedZipCodeForNineDigits(){
        //Arrange
        String postalCode = "100011234";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("UNITED STATES");
        String expected = "10001-1234";
        //Act
        USZipCode zip = new USZipCode(country, postalCode);
        String result = zip.getValue();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNormalizedZipCodeReplacingSpaces(){
        //Arrange
        String postalCode = "10001 1234";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("UNITED STATES");
        String expected = "10001-1234";
        //Act
        USZipCode zip = new USZipCode(country, postalCode);
        String result = zip.getValue();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowForNullCountry(){
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new USZipCode(null, "10001"));
    }

    @Test
    void shouldThrowForNullPostalCode(){
        //Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("UNITED STATES");
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new USZipCode(country, null));
        //Assert
        assertEquals("Postal code cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowForCountryNotUnitedStates(){
        //Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new USZipCode(country, "10001"));
        //Assert
        assertEquals("Postal code must belong to the United States", exception.getMessage());
    }

    @Test
    void shouldThrowForInvalidZipCodeLength(){
        //Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("UNITED STATES");
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new USZipCode(country, "1234"));
        //Assert
        assertEquals("US ZIP code must have 5 or 9 digits", exception.getMessage());
    }

    @Test
    void shouldThrowForInvalidZipCodePattern(){
        //Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("UNITED STATES");
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new USZipCode(country, "ABCDE"));
        //Assert
        assertEquals("US ZIP code must have 5 or 9 digits", exception.getMessage());
    }

}