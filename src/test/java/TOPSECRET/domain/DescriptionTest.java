package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DescriptionTest {

    //Test constructor
    @Test
    public void validDescription() {
        Description description = new Description("Amazing Book");
        assertEquals("Amazing Book", description.getDescription());
        assertEquals(12, description.getLength());
    }
    @Test
    public void validDescriptionWithSpecialCharacters() {
        Description description = new Description("Good condition, book on plants.");
        assertEquals("Good condition, book on plants.", description.getDescription());
        assertEquals(31, description.getLength());
    }
    @Test
    public void constructorTrimSpaces() {
        Description description = new Description("   Extraordinary sci-fy magazine!   ");
        assertEquals("Extraordinary sci-fy magazine!", description.getDescription());
        assertEquals(30, description.getLength());
    }
    @Test
    public void maxLengthAccepted() {
        String maxLengthDescription = "a".repeat(Description.MAX_LENGTH);
        Description description = new Description(maxLengthDescription);
        assertEquals(Description.MAX_LENGTH, description.getLength());
    }
    @Test
    public void setUpdatedDescriptionAndTrim() {
        Description description = new Description("Original Description");
        description.setDescription("New Description!  ");
        assertEquals("New Description!", description.getDescription());
        assertEquals(16, description.getLength());
    }

    //Invalid cases
    @Test
    public void constructor_nullThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Description(null));
        assertEquals("Description cannot be empty!", exception.getMessage());
    }
    @Test
    public void constructor_emptyThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Description(""));
        assertEquals("Description cannot be empty!", exception.getMessage());
    }
    @Test
    public void constructor_spacesOnlyThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Description("   "));
        assertEquals("Description cannot be empty!", exception.getMessage());
    }
    @Test
    public void constructor_tooLongThrowsException() {
        String tooLongDescription = "a".repeat(Description.MAX_LENGTH + 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Description(tooLongDescription));
        assertTrue(exception.getMessage().contains("Description too long (maximum of " + Description.MAX_LENGTH + " characters)"));
    }
    @Test
    public void setter_emptyThrowsException() {
        Description description = new Description("Original Description");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> description.setDescription(""));
        assertEquals("Description cannot be empty!", exception.getMessage());
    }
    @Test
    public void setter_spacesOnlyThrowsException() {
        Description description = new Description("Original Description");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> description.setDescription("   "));
        assertEquals("Description cannot be empty!", exception.getMessage());
    }
    @Test
    public void setter_tooLongThrowsException() {
        Description description = new Description("Original Description");
        String tooLongDescription = "a".repeat(Description.MAX_LENGTH + 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> description.setDescription(tooLongDescription));
        assertTrue(exception.getMessage().contains("Description too long (maximum of " + Description.MAX_LENGTH + " characters)"));
    }

    @Test
    public void getLength() {
        Description description = new Description("Item in perfect condition at only 9.99€!");
        assertEquals(40, description.getLength());
    }
    @Test
    public void toString() {
        Description description = new Description("Amazing Magazine!");
        assertEquals("Amazing Magazine! (17/500)", description.toString());
    }
}