package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void NewValidLocation() throws InstantiationException
    {
        // arrange
        String strStreet = "Rua de S. Tomé, s/n";
        String strPostalCode = "4200 Porto";

        // act
        Location location = new Location( strStreet, strPostalCode );

        // assert
        assertEquals(location.getStreet(), strStreet);
        assertEquals(location.getPostalCode(), strPostalCode);
    }

    @Test
    void NewInvalidStreetLocation()
    {
        // arrange
        String strStreet = "";
        String strPostalCode = "4200 Porto";
        String expectedMessage = "Invalid Street or Postal Code";

        // act + assert
        Exception exception = assertThrows(InstantiationException.class, () ->
            new Location( strStreet, strPostalCode )
        );

        // assert
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void NewInvalidPostalCodeLocation()
    {
        // arrange
        String strStreet = "Rua de S. Tomé, s/n";
        String strPostalCode = null;
        String expectedMessage = "Invalid Street or Postal Code";

        // act + assert
        Exception exception = assertThrows(InstantiationException.class, () ->
            new Location( strStreet, strPostalCode )
        );

        // assert
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void NewInvalidStreetAndPostalCodeLocation()
    {
        // arrange
        String strStreet = null;
        String strPostalCode = null;
        String expectedMessage = "Invalid Street or Postal Code";

        // act + assert
        Exception exception = assertThrows(InstantiationException.class, () ->
            new Location( strStreet, strPostalCode )
        );

        // assert
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}