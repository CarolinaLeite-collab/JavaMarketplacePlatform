package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryFactoryTest {

    @Test
    void shouldSuccessfullyCreateCountry () throws InstantiationException {
        // arrange
        String countryName = "Deutschland";
        CountryFactory factory = new CountryFactory();
        // act
        Country newCountry = factory.create(countryName);
        //assert
        assertEquals(countryName,newCountry.getCountryName());
    }
    @Test
    void shouldThrowExceptionWhenCreateCountry () throws InstantiationException {
        // arrange
        String countryName = "";
        CountryFactory factory = new CountryFactory();
        // act and assert
        assertThrows(InstantiationException.class,() -> factory.create(countryName));
    }
}