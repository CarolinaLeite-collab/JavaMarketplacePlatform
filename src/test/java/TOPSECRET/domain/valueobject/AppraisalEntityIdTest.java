package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppraisalEntityIdTest {

    @Test
    void shouldThrowExceptionWhenNameIsNull() {

        // Arrange
        String name = null;

        // Act & Assert & SUT
        assertThrows(IllegalArgumentException.class, () -> new AppraisalEntityId(name));

    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {

        // Arrange
        String name = "   ";

        // Act & Assert & SUT
        assertThrows(IllegalArgumentException.class,() -> new AppraisalEntityId(name));

    }

    @Test
    void shouldGenerateCorrectIdSingleWord() {

        // Arrange
        String name = "Library Inspectors";

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:LI-LibraryInspectors", id.toString());

    }

    @Test
    void shouldGenerateCorrectIdMultipleWords() {

        // Arrange
        String name = "The Book Watchers";

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:TBW-TheBookWatchers", id.toString());

    }

    @Test
    void shouldIgnoreExtraSpaces() {

        // Arrange
        String name = "  The   Book   Watchers  ";

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:TBW-TheBookWatchers", id.toString());

    }

    @Test
    void shouldHandleMultipleSpacesBetweenWords() {

        // Arrange
        String name = "The    Book     Watchers";

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:TBW-TheBookWatchers", id.toString());

    }

    @Test
    void shouldHandleTabsAndSpecialWhitespace() {

        // Arrange
        String name = "\tThe Book Watchers\t";

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:TBW-TheBookWatchers", id.toString());

    }

    @Test
    void shouldNormalizeUpperCaseName() {

        // Arrange
        String name = "BOOK LOVERS";

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:BL-BookLovers", id.toString());

    }

    @Test
    void shouldNormalizeLowerCaseName() {

        // Arrange
        String name = "book lovers";

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:BL-BookLovers", id.toString());

    }

    @Test
    void shouldBeEqualWhenSameName() {

        // Arrange
        String name = "The Book Watchers";

        // Act & SUT
        AppraisalEntityId id1 = new AppraisalEntityId(name);
        AppraisalEntityId id2 = new AppraisalEntityId(name);

        // Assert
        assertEquals(id1, id2);

    }

    @Test
    void shouldBeEqualToItself() {

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId("Book Lovers");

        // Assert
        assertTrue(id.equals(id));

    }

    @Test
    void shouldNotBeEqualToNull() {

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId("Book Lovers");

        // Assert
        assertFalse(id.equals(null));

    }

    @Test
    void shouldNotBeEqualToDifferentType() {

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId("Book Lovers");

        // Assert
        assertFalse(id.equals("entity:BL-BookLovers"));

    }

    @Test
    void shouldHaveSameHashCodeWhenEqual() {

        // Arrange
        String name = "The Book Watchers";

        // Act & SUT
        AppraisalEntityId id1 = new AppraisalEntityId(name);
        AppraisalEntityId id2 = new AppraisalEntityId(name);

        int hash1 = id1.hashCode();
        int hash2 = id2.hashCode();

        // Assert
        assertEquals(hash1, hash2);

    }

    @Test
    void shouldBeCaseInsensitiveInNameProcessing() {

        // Arrange
        String name1 = "the book watchers";
        String name2 = "THE BOOK WATCHERS";

        // Act & SUT
        AppraisalEntityId id1 = new AppraisalEntityId(name1);
        AppraisalEntityId id2 = new AppraisalEntityId(name2);

        // Assert
        assertTrue(id1.equals(id2));

    }

    @Test
    void shouldKeepFirstLetterUpperCaseInNormalizedPart() {

        // Arrange
        String name = "book lovers";

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:BL-BookLovers", id.toString());

    }

    @Test
    void shouldHandleSingleWordName() {

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId("Book");

        // Assert
        assertEquals("entity:B-Book", id.toString());

    }

    @Test
    void shouldHandleSingleLetterName() {

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId("A");

        // Assert
        assertEquals("entity:A-A", id.toString());

    }

    @Test
    void shouldHandleAccentedCharacters() {

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId("Coração Livre");

        // Assert
        assertEquals("entity:CL-CoraçãoLivre", id.toString());

    }

    @Test
    void shouldHandleHyphenatedWords() {

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId("Book-Watchers Society");

        // Assert
        assertEquals("entity:BWS-BookWatchersSociety", id.toString());

    }

    @Test
    void shouldHandleNamesWithNumbers() {

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId("Team 42 Book Lovers");

        // Assert
        assertEquals("entity:T4BL-Team42BookLovers", id.toString());

    }

    @Test
    void shouldBeEqualIfToStringIsEqual() {

        // Act & SUT
        AppraisalEntityId id1 = new AppraisalEntityId("Book Lovers");
        AppraisalEntityId id2 = new AppraisalEntityId("Book Lovers");

        // Assert
        assertEquals(id1.toString(), id2.toString());
        assertTrue(id1.equals(id2));

    }

    @Test
    void equalObjectsMustHaveSameHashCode() {

        // Arrange & SUT
        AppraisalEntityId id1 = new AppraisalEntityId("The Book Watchers");
        AppraisalEntityId id2 = new AppraisalEntityId("The Book Watchers");

        // Act
        int hash1 = id1.hashCode();
        int hash2 = id2.hashCode();

        // Assert
        assertEquals(hash1, hash2);

    }

    @Test
    void differentNamesShouldHaveDifferentHashCodes() {

        // Arrange & SUT
        AppraisalEntityId id1 = new AppraisalEntityId("Book Lovers");
        AppraisalEntityId id2 = new AppraisalEntityId("Book Watchers");

        // Act
        int hash1 = id1.hashCode();
        int hash2 = id2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);

    }

}
