package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppraisalEntityIdTest {

    @Test
    void testAConstructorWithName() {

        // Arrange
        Name name = mock(Name.class);

        // Act
        new AppraisalEntityId(name);

    }

    @Test
    void testAConstructorWithString() {

        // Act
        new AppraisalEntityId("livro");

    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new AppraisalEntityId((Name) null));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("    ");

        // Act & Assert & SUT
        assertThrows(IllegalArgumentException.class,() -> new AppraisalEntityId(name));

    }

    @Test
    void shouldGenerateCorrectIdSingleWord() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("Library Inspectors");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:LI-LibraryInspectors", id.toString());

    }

    @Test
    void shouldGenerateCorrectIdMultipleWords() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("The Book Watchers");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:TBW-TheBookWatchers", id.toString());

    }

    @Test
    void shouldIgnoreExtraSpaces() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("  The   Book   Watchers  ");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:TBW-TheBookWatchers", id.toString());

    }

    @Test
    void shouldHandleMultipleSpacesBetweenWords() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("The    Book     Watchers");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:TBW-TheBookWatchers", id.toString());

    }

    @Test
    void shouldHandleTabsAndSpecialWhitespace() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("\tThe Book Watchers\t");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:TBW-TheBookWatchers", id.toString());

    }

    @Test
    void shouldNormalizeUpperCaseName() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("BOOK LOVERS");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:BL-BookLovers", id.toString());

    }

    @Test
    void shouldNormalizeLowerCaseName() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("book lovers");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:BL-BookLovers", id.toString());

    }

    @Test
    void shouldBeEqualWhenSameName() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("livro");

        // Act & SUT
        AppraisalEntityId id1 = new AppraisalEntityId(name);
        AppraisalEntityId id2 = new AppraisalEntityId(name);

        // Assert
        assertEquals(id1, id2);

    }

    @Test
    void shouldBeEqualToItself() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("livro");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertTrue(id.equals(id));

    }

    @Test
    void shouldNotBeEqualToNull() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("livro");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertFalse(id.equals(null));

    }

    @Test
    void shouldNotBeEqualToDifferentType() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("book lovers");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertFalse(id.equals("entity:BL-BookLovers"));

    }

    @Test
    void shouldHaveSameHashCodeWhenEqual() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("livro");

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
        Name name1 = mock(Name.class);
        when(name1.toString()).thenReturn("the book watchers");
        Name name2 = mock(Name.class);
        when(name2.toString()).thenReturn("THE BOOK WATCHERS");

        // Act & SUT
        AppraisalEntityId id1 = new AppraisalEntityId(name1);
        AppraisalEntityId id2 = new AppraisalEntityId(name2);

        // Assert
        assertTrue(id1.equals(id2));

    }

    @Test
    void shouldKeepFirstLetterUpperCaseInNormalizedPart() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("book-lovers");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:BL-BookLovers", id.toString());

    }

    @Test
    void shouldHandleSingleWordName() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("Book");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:B-Book", id.toString());

    }

    @Test
    void shouldHandleSingleLetterName() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("A");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:A-A", id.toString());

    }

    @Test
    void shouldHandleAccentedCharacters() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("Coração Livre");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:CL-CoraçãoLivre", id.toString());

    }

    @Test
    void shouldHandleHyphenatedWords() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("Book-Watchers Society");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:BWS-BookWatchersSociety", id.toString());

    }

    @Test
    void shouldHandleNamesWithNumbers() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("Team 42 Book Lovers");

        // Act & SUT
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertEquals("entity:T4BL-Team42BookLovers", id.toString());

    }

    @Test
    void shouldBeEqualIfToStringIsEqual() {

        // Arrange
        Name name1 = mock(Name.class);
        when(name1.toString()).thenReturn("livro");
        Name name2 = mock(Name.class);
        when(name2.toString()).thenReturn("livro");

        // Act & SUT
        AppraisalEntityId id1 = new AppraisalEntityId(name1);
        AppraisalEntityId id2 = new AppraisalEntityId(name2);

        // Assert
        assertEquals(id1.toString(), id2.toString());
        assertTrue(id1.equals(id2));

    }

    @Test
    void equalObjectsMustHaveSameHashCode() {

        // Arrange
        Name name1 = mock(Name.class);
        when(name1.toString()).thenReturn("livro");
        Name name2 = mock(Name.class);
        when(name2.toString()).thenReturn("livro");

        // Arrange & SUT
        AppraisalEntityId id1 = new AppraisalEntityId(name1);
        AppraisalEntityId id2 = new AppraisalEntityId(name2);

        // Act
        int hash1 = id1.hashCode();
        int hash2 = id2.hashCode();

        // Assert
        assertEquals(hash1, hash2);

    }

    @Test
    void differentNamesShouldHaveDifferentHashCodes() {

        // Arrange
        Name name1 = mock(Name.class);
        when(name1.toString()).thenReturn("livro1");
        Name name2 = mock(Name.class);
        when(name2.toString()).thenReturn("livro2");

        // Arrange & SUT
        AppraisalEntityId id1 = new AppraisalEntityId(name1);
        AppraisalEntityId id2 = new AppraisalEntityId(name2);

        // Act
        int hash1 = id1.hashCode();
        int hash2 = id2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);

    }

}
