package MITELOVERS.domain.author;

import MITELOVERS.domain.valueobject.AuthorId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class AuthorTest {

    @Test
    void testConstructor() {

        // Arrange
        String name = "Eça de Queirós";

        // Act & SUT
        new Author(name);

    }

    @Test
    void testConstructorWithId() {

        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        String name = "Eça de Queirós";

        // Act & SUT
        new Author(authorId, name);

    }

    @Test
    void validNameAuthor() {

        // Arrange
        String name = "Eça de Queirós";

        // Act & SUT
        Author a = new Author(name);

        // Assert
        assertEquals("Eça de Queirós", a.getName());

    }

    @Test
    void validNameAuthorWithId() {

        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        String name = "Eça de Queirós";

        // Act & SUT
        Author a = new Author(authorId, name);

        // Assert
        assertEquals("Eça de Queirós", a.getName());
        assertNotNull(a.identity());

    }

    @Test
    void authorNameIsTrimmed() {

        // Arrange
        String name = " Eça de Queirós ";

        // Act & SUT
        Author a = new Author(name);

        // Assert
        assertEquals("Eça de Queirós", a.getName());

    }

    @Test
    void capitalizationNameTest() {

        // Arrange & SUT
        Author a2 = new Author("Eça De Queirós");
        Author a3 = new Author("EÇA DE QUEIRÓS");
        Author a4 = new Author("eça de queirós");

        // Act & Assert
        assertEquals(a2.getLowerCaseName(), a3.getLowerCaseName());
        assertEquals(a2.getLowerCaseName(), a4.getLowerCaseName());

    }

    @Test
    void authorNameIsTrimmedAndLowerCased() {

        // Arrange & SUT
        Author a = new Author("  EÇA DE QUEIRÓS  ");

        // Act
        String lowerName = a.getLowerCaseName();

        // Assert
        assertEquals("eça de queirós", lowerName);

    }

    @Test
    void rejectEmptyNameAuthor() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Author("   "));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmptyWithAuthorId() {

        // Arrange
        AuthorId authorId = mock(AuthorId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Author(authorId, "   "));
    }

    @Test
    void rejectNullNameAuthor() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Author(null));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNameIsNull() {

        // Arrange
        AuthorId authorId = mock(AuthorId.class);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new Author(authorId, null));
    }


    @Test
    void testEqualsWithSameObject() {

        // Arrange & SUT
        Author a = new Author("Seneca");

        // Act & Assert
        assertTrue(a.equals(a));

    }

    @Test
    void equalsShouldReturnFalseWhenNull() {

        // Arrange & SUT
        Author a = new Author("Seneca");

        // Act & Assert
        assertFalse(a.equals(null));
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentType() {

        // Arrange & SUT
        Author a = new Author("Seneca");

        // Act & Assert
        assertFalse(a.equals("string"));
    }

    @Test
    void equalsShouldReturnTrueForSameAuthorId() {

        // Arrange
        AuthorId id = mock(AuthorId.class);

        // Arrange & SUT
        Author a1 = new Author(id, "Seneca");
        Author a2 = new Author(id, "Seneca");

        // Act & Assert
        assertTrue(a1.equals(a2));
    }

    @Test
    void equalsShouldReturnFalseForDifferentAuthorIds() {

        // Arrange
        AuthorId id1 = mock(AuthorId.class);
        AuthorId id2 = mock(AuthorId.class);

        // Arrange & SUT
        Author a1 = new Author(id1, "Seneca");
        Author a2 = new Author(id2, "Seneca");

        // Act & Assert
        assertFalse(a1.equals(a2));
    }

    @Test
    void testEqualHashCodeWhenSameAuthorId() {

        // Arrange
        AuthorId id = mock(AuthorId.class);

        // Arrange & SUT
        Author a = new Author(id, "Seneca");
        Author a2 = new Author(id, "SeneCA");

        // Act & Assert
        assertEquals(a.hashCode(), a2.hashCode());

    }

    @Test
    void testNonEqualHashCodeWhenDifferentAuthorId() {

        // Arrange
        AuthorId id1 = mock(AuthorId.class);
        AuthorId id2 = mock(AuthorId.class);

        // Arrange & SUT
        Author a = new Author(id1, "Seneca");
        Author a2 = new Author(id2, "SeneCAR");

        // Act & Assert
        assertNotEquals(a.hashCode(), a2.hashCode());

    }

    @Test
    void identityShouldReturnAuthorId() {

        // Arrange & SUT
        Author author = new Author("Seneca");

        // Act
        AuthorId id = author.identity();

        // Assert
        assertNotNull(id);

    }

    @Test
    void identityShouldBeNotEqualForSameName() {

        // Arrange & SUT
        Author a1 = new Author("Seneca");
        Author a2 = new Author("Seneca");

        // Act & Assert
        assertNotEquals(a1.identity(), a2.identity());

    }

    @Test
    void identityShouldBeDifferentForDifferentNames() {

        // Arrange & SUT
        Author a1 = new Author("Seneca");
        Author a2 = new Author("Justinian");

        // Act & Assert
        assertNotEquals(a1.identity(), a2.identity());

    }

    @Test
    void sameAsShouldReturnTrueIgnoringCase() {

        // Arrange & SUT
        Author a1 = new Author("Seneca");
        Author a2 = new Author("SENECA");

        // Act
        boolean result = a1.sameAs(a2);

        // Assert
        assertTrue(result);

    }

    @Test
    void sameAsShouldReturnFalseForDifferentNames() {

        // Arrange & SUT
        Author a1 = new Author("Seneca");
        Author a2 = new Author("Justinian");

        // Act
        boolean result = a1.sameAs(a2);

        // Assert
        assertFalse(result);

    }

    @Test
    void sameAsShouldReturnFalseWhenNull() {

        // Arrange & SUT
        Author a = new Author("Seneca");

        // Act
        boolean result = a.sameAs(null);

        // Assert
        assertFalse(result);

    }

    @Test
    void sameAsShouldReturnFalseForDifferentType() {

        // Arrange & SUT
        Author a = new Author("Seneca");

        // Act
        boolean result = a.sameAs("Seneca");

        // Assert
        assertFalse(result);

    }

    @Test
    void sameAsShouldReturnTrueForSameObject() {

        // Arrange & SUT
        Author a = new Author("Seneca");

        // Act
        boolean result = a.sameAs(a);

        // Assert
        assertTrue(result);

    }
}
