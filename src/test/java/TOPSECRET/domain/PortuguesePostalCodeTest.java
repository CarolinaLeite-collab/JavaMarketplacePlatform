package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PortuguesePostalCodeTest {
    @Test
    void shouldInstantiatePortuguesePostalCodeWithValidData(){
        //Arrange
        String postalCode = "3720-748";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");
        String expected = "PORTUGAL";
        //Act
        PortuguesePostalCode ptPostalCode = new PortuguesePostalCode(country,postalCode);
        String result = ptPostalCode.getPostalCodeCountry().getCountryName();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldInstantiatePortuguesePostalCodeWithValidDataWithExtraSpaces(){
        //Arrange
        String postalCode = "3720 - 748";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");
        String expected = "3720-748";
        //Act
        PortuguesePostalCode ptPostalCode = new PortuguesePostalCode(country,postalCode);
        String result = ptPostalCode.getValue();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNormalizedPostalCodeReplacingSpaceswithDash(){
        //Arrange
        String postalCode = "3720 748";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");
        String expected = "3720-748";
        //Act
        PortuguesePostalCode ptPostalCode = new PortuguesePostalCode(country,postalCode);
        String result = ptPostalCode.getValue();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNormalizedPostalCodeReplacingIntroducingDash(){
        //Arrange
        String postalCode = "3720748";
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");
        String expected = "3720-748";
        //Act
        PortuguesePostalCode ptPostalCode = new PortuguesePostalCode(country,postalCode);
        String result = ptPostalCode.getValue();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowForNullCountry(){
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                ()-> new PortuguesePostalCode(null,"3720-748"));
    }

    @Test
    void shouldThrowForNullPostalCode(){
        //Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()-> new PortuguesePostalCode(country,null));
        //Assert
        assertEquals("Postal code cannot be null",exception.getMessage());
    }

    @Test
    void shouldThrowForCountryNotPortugal(){
        //Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("GERMANY");
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()-> new PortuguesePostalCode(country,"3720-748"));
        //Assert
        assertEquals("Postal code must belong to Portugal",exception.getMessage());
    }

    @Test
    void shouldThrowForInvalidPostalCodePattern(){
        //Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()-> new PortuguesePostalCode(country,"345"));
        //Assert
        assertEquals("Portuguese postal code has 7 digits", exception.getMessage());
    }

    @Test
    void shouldThrowForInvalidPostalCodePatternWithLetters(){
        //Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()-> new PortuguesePostalCode(country,"0720-748"));
        //Assert
        assertEquals("Invalid Portuguese postal code", exception.getMessage());
    }

    @Test
    void shouldThrowForEmptyPostalCode(){
        //Arrange
        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("PORTUGAL");
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()-> new PortuguesePostalCode(country," "));
        //Assert
        assertEquals("Portuguese postal code has 7 digits", exception.getMessage());
    }
}