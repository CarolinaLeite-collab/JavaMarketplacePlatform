package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DescriptionTest {

    private String _maxLengthText;
    private String _tooLongText;

    @BeforeEach
    void setUp() {
        _maxLengthText = "a".repeat(Description.MAX_LENGTH);
        _tooLongText = "a".repeat(Description.MAX_LENGTH + 1);
    }

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
        Description description = new Description(_maxLengthText);
        assertEquals(Description.MAX_LENGTH, description.getLength());
    }

    @Test
    public void setUpdatedDescriptionAndTrim() {
        Description description = new Description("Original Description");
        description.setDescription("New Description!  ");
        assertEquals("New Description!", description.getDescription());
        assertEquals(16, description.getLength());
    }

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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Description(_tooLongText));
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
    public void setter_nullThrowsException() {
        Description description = new Description("Original Description");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> description.setDescription(null));
        assertEquals("Description cannot be empty!", exception.getMessage());
    }

    @Test
    public void setter_tooLongThrowsException() {
        Description description = new Description("Original Description");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> description.setDescription(_tooLongText));
        assertTrue(exception.getMessage().contains("Description too long (maximum of " + Description.MAX_LENGTH + " characters)"));
    }

    @Test
    public void setter_acceptsMaxLength() {
        Description description = new Description("Original Description");
        description.setDescription(_maxLengthText);
        assertEquals(Description.MAX_LENGTH, description.getLength());
        assertEquals(_maxLengthText, description.getDescription());
    }

    @Test
    public void toStringReflectsUpdatedDescription() {
        Description description = new Description("Short");
        description.setDescription("New text");
        assertEquals("New text (8/500)", description.toString());
    }

    @Test
    public void setter_acceptsMaxLengthWithSurroundingSpaces() {
        Description description = new Description("Original Description");
        String maxWithSpaces = "  " + _maxLengthText + "  ";
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> description.setDescription(maxWithSpaces));
        assertTrue(expected.getMessage().contains("Description too long"));
    }

    @Test
    public void getLength() {
        Description description = new Description("Item in perfect condition at only 9.99€!");
        assertEquals(40, description.getLength());
    }

    @Test
    public void testToString() {
        Description description = new Description("Amazing Magazine!");
        assertEquals("Amazing Magazine! (17/500)", description.toString());
    }
}

