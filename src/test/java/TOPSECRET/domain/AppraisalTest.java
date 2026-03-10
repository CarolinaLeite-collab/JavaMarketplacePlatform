package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppraisalTest {

    private static final LocalDateTime _fixedDate =  LocalDateTime.of(2025, 1, 1, 10, 0);
    private static final String _validDescription =  "The book is in great condition, with only slight use marks.";

    @Test
    void creationOfValidAppraisalShouldSucceed() {

        // Arrange
        Price priceDouble = mock(Price.class);

        // Act
        Appraisal appraisal = new Appraisal(priceDouble, _fixedDate,_validDescription);

        // Assert
        assertEquals(priceDouble, appraisal.getValueEstimate());
        assertEquals(_fixedDate, appraisal.getAppraisalDate());
        assertEquals(_validDescription, appraisal.getObjectDescription());
    }

    @Test
    void constructorShouldThrowWhenValueEstimateIsNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(null, _fixedDate, _validDescription));
    }

    @Test
    void constructorShouldThrowWhenAppraisalDateIsNull() {

        // Arrange
        Price priceDouble = mock(Price.class); // stub

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(priceDouble, null, _validDescription));
    }

    @Test
    void constructorShouldThrowWhenDescriptionIsEmpty() {

        // Arrange
        Price priceDouble = mock(Price.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(priceDouble, _fixedDate, ""));
    }
    @Test
    void constructorShouldThrowWhenDescriptionIsBlank() {

        // Arrange
        Price priceDouble = mock(Price.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(priceDouble, _fixedDate, "   ")); //SUT
    }

    @Test
    void constructorShouldThrowWhenDescriptionIsNull() {
        // Arrange
        Price priceDouble = mock(Price.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Appraisal(priceDouble, _fixedDate, null)); // SUT
    }

    @Test
    void equalAppraisalsShouldBeEqual () {

        // Arrange
        Price priceDouble = mock(Price.class);
        Appraisal a1 = new Appraisal(priceDouble, _fixedDate,_validDescription);
        Appraisal a2 = new Appraisal(priceDouble, _fixedDate,_validDescription);

        // Act & Assert
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void appraisalsWithDifferentFieldsShouldHaveDifferentHashCodes() {

        // Arrange
        Price priceDouble1 = mock(Price.class);
        Price priceDouble2 = mock(Price.class);
        LocalDateTime otherDate = LocalDateTime.of(2025, 6, 1, 10, 0);
        Appraisal a1 = new Appraisal(priceDouble1, _fixedDate, _validDescription);
        Appraisal a2 = new Appraisal(priceDouble2, otherDate, "Other description");

        // Act & Assert
        assertNotEquals(a1.hashCode(), a2.hashCode()); // SUT
    }

    @Test
    void appraisalsWithDifferentPriceShouldNotBeEqual() {

        // Arrange
        Price priceDouble = mock(Price.class);
        Price priceDouble1 = mock(Price.class);
        Appraisal a1 = new Appraisal(priceDouble, _fixedDate, _validDescription );
        Appraisal differentPrice = new Appraisal(priceDouble1, _fixedDate, _validDescription );

        // Act & Assert
        assertNotEquals(a1, differentPrice);
    }

    @Test
    void appraisalsWithDifferentDateShouldNotBeEqual() {

        // Arrange
        Price priceDouble = mock(Price.class);
        LocalDateTime otherDate = LocalDateTime.of(2025, 1, 5, 10, 0);
        Appraisal a1 = new Appraisal(priceDouble, _fixedDate, _validDescription );
        Appraisal differentDate = new Appraisal(priceDouble, otherDate , _validDescription );

        // Act & Assert
        assertNotEquals(a1, differentDate);
    }

    @Test
    void appraisalsWithDifferentDescriptionShouldNotBeEqual() {

        // Arrange
        Price priceDouble = mock(Price.class);
        Appraisal a1 = new Appraisal(priceDouble, _fixedDate, _validDescription);
        Appraisal a2 = new Appraisal(priceDouble, _fixedDate, "Different description");

        // Act & Assert
        assertNotEquals(a1, a2);
    }


    @Test
    void appraisalShouldNotEqualNull() {
        // Arrange
        Price priceDouble = mock(Price.class); // stub
        Appraisal appraisal = new Appraisal(priceDouble, _fixedDate, _validDescription);

        // Act & Assert
        assertNotEquals(null, appraisal); // SUT
    }

    @Test
    void appraisalShouldEqualItself() {

        // Arrange
        Price priceDouble = mock(Price.class); // stub
        Appraisal appraisal = new Appraisal(priceDouble, _fixedDate, _validDescription);

        // Act & Assert
        assertEquals(appraisal, appraisal); // SUT
    }

    @Test
    void appraisalShouldNotEqualDifferentType() {

        // Arrange
        Price priceDouble = mock(Price.class); // stub
        Appraisal appraisal = new Appraisal(priceDouble, _fixedDate, _validDescription);

        // Act & Assert
        assertFalse(appraisal.equals("not an appraisal")); // SUT
    }

    @Test
    void toStringShouldContainKeyInformation() {

        // Arrange
        Price priceDouble = mock(Price.class);
        when(priceDouble.toString()).thenReturn("10.0 €");
        Appraisal appraisal = new Appraisal(priceDouble, _fixedDate, _validDescription );

        // Act
        String text = appraisal.toString();

        // Assert
        assertTrue(text.contains("Appraisal"));
        assertTrue(text.contains(_validDescription));
        assertTrue(text.contains("10.0 €"));
        assertTrue(text.contains("2025-01-01"));
    }

}
