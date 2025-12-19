package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchoolTest
{
    @Test
    void defineValidLocation() throws InstantiationException
    {
        // arrange
        School school = new School("ISEP");

        String strStreet = "Rua de S. Tomé, s/n";
        String strPostalCode = "4200 Porto";

        // act
        Location location = school.defineLocation( strStreet, strPostalCode );

        // assert
        assertEquals(location.getStreet(), strStreet);
        assertEquals(location.getPostalCode(), strPostalCode);

        // how to check location set in house?
    }

    @Test
    void defineInvalidStreetLocation()
    {
        // arrange
        School school = new School("ISEP");

        String strStreet = "";
        String strPostalCode = "4200 Porto";
        String expectedMessage = "Invalid Street or Postal Code";

        // act + assert
        Exception exception = assertThrows(InstantiationException.class, () ->
            school.defineLocation( strStreet, strPostalCode )
        );

        // assert
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void defineInvalidPostalCodeLocation()
    {
        // arrange
        School school = new School("ISEP");

        String strStreet = "Rua de S. Tomé, s/n";
        String strPostalCode = null;
        String expectedMessage = "Invalid Street or Postal Code";

        // act + assert
        Exception exception = assertThrows(InstantiationException.class, () ->
            school.defineLocation( strStreet, strPostalCode )
        );

        // assert
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void defineInvalidStreetAndPostalCodeLocation()
    {
        // arrange
        School school = new School("ISEP");

        String strStreet = null;
        String strPostalCode = null;
        String expectedMessage = "Invalid Street or Postal Code";

        // act + assert
        Exception exception = assertThrows(InstantiationException.class, () ->
            school.defineLocation( strStreet, strPostalCode )
        );

        // assert
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void defineValidSchoolName() {
        // arrange + act
        School school = new School("ISEP");

        // assert
        assertEquals("ISEP", school.getName());
    }
}