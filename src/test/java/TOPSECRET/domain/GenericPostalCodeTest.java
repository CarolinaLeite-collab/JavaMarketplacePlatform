package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenericPostalCodeTest {
    @Test
    void shouldInstantiateGenericPostalCodeWithValidData() {
        //Arrange
        String postalCode = "37200";
        Country country = mock(Country.class);
        //Act
        GenericPostalCode generic = new GenericPostalCode(country, "37200");
        //Assert
        assertEquals(country, generic.getPostalCodeCountry());
    }
    @Test
    void shouldReturnPostalCodeValue(){
        //Arrange
        String postalCode = "37200";
        Country country = mock(Country.class);
        //Act
        GenericPostalCode generic = new GenericPostalCode(country, postalCode);
        String result = generic.getValue();
        //Assert
        assertEquals("37200", result);
    }

    @Test
    void shouldReturnTrimmedPostalCode(){
        //Arrange
        String postalCode = " 37200 ";
        Country country = mock(Country.class);
        String expected = "37200";
        //Act
        GenericPostalCode generic = new GenericPostalCode(country, postalCode);
        String result = generic.getValue();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowForNullPostalCode(){
        //Arrange
        Country country = mock(Country.class);
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new GenericPostalCode(country, null));
        //Assert
        assertEquals("Postal code cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldThrowForBlankPostalCode(){
        //Arrange
        Country country = mock(Country.class);
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new GenericPostalCode(country, "   "));
        //Assert
        assertEquals("Postal code cannot be null or blank", exception.getMessage());
    }
}