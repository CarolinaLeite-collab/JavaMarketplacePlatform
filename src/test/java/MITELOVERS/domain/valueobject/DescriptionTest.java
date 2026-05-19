package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DescriptionTest {

    @Test
    public void constructorCreatesValidDescription() {

        //Act
        Description description = new Description("Amazing Book");

        //Assert
        assertEquals("Amazing Book", description.toString());
    }

    @Test
    public void constructorThrowsExceptionWhenNull() {

        //Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Description(null));
        assertEquals("Description cannot be null!", exception.getMessage());
    }

    @Test
    public void constructorThrowsExceptionWhenEmpty() {

        //Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Description(""));
        assertEquals("Description cannot be empty or blank!", exception.getMessage());
    }

    @Test
    public void constructorThrowsExceptionWhenBlank() {

        //Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Description("   "));
        assertEquals("Description cannot be empty or blank!", exception.getMessage());
    }

    @Test
    public void constructorThrowsExceptionWhenTooLongDescription() {

        //Arrange
        String tooLongText = "a".repeat(Description.MAX_LENGTH + 1);

        //Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Description(tooLongText));
        assertTrue(exception.getMessage().contains("Description too long (maximum of " + Description.MAX_LENGTH + " characters)"));
    }

    @Test
    public void getLengthReturnsTrimmedLength() {
        Description description = new Description(" Nice!      ");
        assertEquals(5, description.getLength());
    }

    @Test
    public void getDetailedDescriptionReturnsDetails() {

        //Act
        Description description = new Description("Amazing Book");

        //Assert
        assertEquals("Amazing Book (12/500)", description.getDetailedDescription());
    }

    @Test
    public void validDescriptionWithSpecialCharacters() {

        //Act
        Description description = new Description("Good condition, book on plants.");

        //Assert
        assertEquals("Good condition, book on plants.", description.toString());
    }

    @Test
    public void constructorTrimSpaces() {

        //Act
        Description description = new Description("   Extraordinary sci-fi magazine!   ");

        //Assert
        assertEquals("Extraordinary sci-fi magazine!", description.toString());
    }

    @Test
    public void maxLengthAccepted() {

        //Arrange
        String maxLengthText = "a".repeat(Description.MAX_LENGTH);

        //Act
        Description description = new Description(maxLengthText);

        //Assert
        assertNotNull(description);
    }

    @Test
    public void equalsShouldReturnTrueForSameObject() {

        //Arrange
        Description description = new Description("Amazing Book");

        //Act
        boolean result = description.equals(description);

        //Assert
        assertTrue(result);

    }

    @Test
    public void equalsShouldReturnTrueForDescriptionsWithSameStringValue() {

        //Arrange
        String description = "Amazing Book";
        Description description1 = new Description(description);
        Description description2 = new Description(description);

        //Act
        boolean result = description1.equals(description2);

        //Assert
        assertTrue(result);
    }

    @Test
    public void equalsShouldReturnFalseForDescriptionsWithDifferentStringValue() {

        //Arrange
        Description description1 = new Description("Amazing book!");
        Description description2 = new Description("Ok book.");

        //Act
        boolean result = description1.equals(description2);

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentType() {

        //Arrange
        Description description = new Description("Pretty interesting magazine, I recommend it.");


        // Act
        boolean result = description.equals("Pretty interesting magazine, I recommend it.");

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForNull() {

        //Arrange
        Description description = new Description("Pretty interesting magazine, I recommend it.");


        // Act
        boolean result = description.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameObjectShouldReturnSameHashCode() {

        //Arrange
        Description description = new Description("Amazing Book");

        //Act + Assert
        assertEquals("Amazing Book".hashCode(), description.hashCode());


    }

}

