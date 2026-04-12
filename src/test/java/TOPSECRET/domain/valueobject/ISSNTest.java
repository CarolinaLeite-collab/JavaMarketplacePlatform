package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ISSNTest {

    @Test
    void shouldStoreAndReturnIssnValue() {
        // Arrange
        ISSN issn = new ISSN("2156-5570");

        // Assert
         assertEquals("21565570", issn.get_issn());
    }

    @Test
    void shouldRejectNullIssn(){
        // act + assert
        assertThrows(IllegalArgumentException.class, () ->
            new ISSN(null));
    }

    @Test
    void constructor_throwsCorrectMessageForNull() {
        //Act
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> new ISSN(null));

        //Assert
        assertEquals("ISSN cannot be null", ex.getMessage());
    }

    @Test
    void constructor_throwsCorrectMessageForInvalidFormat() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> new ISSN("12345678"));

        assertEquals("Invalid ISSN format", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234-56B8", "12A4-5678"})
    void shouldRejectIssnWithLetters(String bad) {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new ISSN(bad));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123-5678", "12342-2678", "1234-678", "1234-56782"})
    void shouldRejectIssnWithWrongLength(String bad) {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new ISSN(bad));
    }

    @Test
    void equals_returnsTrueForSameIssn(){
        // arrange
        ISSN issn = new ISSN("21565570");
        // assert
        assertTrue(issn.equals(issn));
    }

    @Test
    void equals_returnsFalseForDiferentIssn(){
        // arrange
        ISSN issn = new ISSN("21565570");
        ISSN issn2 = new ISSN("03178471");
        // assert
        assertFalse(issn.equals(issn2));
    }

    @Test
    void equals_returnsTrueForSameIssnDifferentFormats() {
        // Arrange
        ISSN a = new ISSN("0317-8471");
        ISSN b = new ISSN("03178471");

        // Assert
        assertEquals(a, b);
    }

    @Test
    void equals_returnsFalseForNull() {
        // arrange
        ISSN a = new ISSN("1234-5679");
        // act + assert
        assertFalse(a.equals(null));
    }

    @Test
    void equals_returnsFalseForDifferentType() {
        // arrange
        ISSN a = new ISSN("1234-5679");
        // act + assert
        assertFalse(a.equals("1234-5679"));
    }
    @Test
    void isValidIssnReturnsFalseWhenNull() {
        // Act + Assert
        assertFalse(ISSN.isValidIssn(null));
    }
    @Test
    void isValidIssnReturnsFalseWhenTooShort() {
        //Act & Assert
        assertFalse(ISSN.isValidIssn("1234567"));
    }

    @Test
    void isValidIssnReturnsTrueWhenEndsWithX() {
        //Act & Assert
        assertTrue(ISSN.isValidIssn("2434561X"));
    }

    @Test
    void isValidIssnReturnsTrueWhenEndsWithDigit() {
        //Act & Assert
        assertTrue(ISSN.isValidIssn("03178471"));
    }

    @Test
    void isValidIssnReturnsFalseWhenLastCharInvalid() {
        //Act & Assert
        assertFalse(ISSN.isValidIssn("0317847A"));
    }

    @Test
    void toStringReturnsNormalizedIssn() {
        // Arrange
        ISSN issn = new ISSN("2156-5570");

        // Act
        String result = issn.toString();

        // Assert
        assertEquals("21565570", result);
    }

    @Test
    void getIdentifierReturnsNormalizedIssn() {
        // Arrange
        ISSN issn = new ISSN("2156-5570");

        // Act
        String result = issn.getIdentifier();

        // Assert
        assertEquals("21565570", result);
    }

}