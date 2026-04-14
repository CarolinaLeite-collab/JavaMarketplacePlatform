package TOPSECRET.domain.author;

import TOPSECRET.domain.valueobject.AuthorId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class AuthorTest {

    @Test
    void testConstructor() {

        // Arrange
        String name = "Eça de Queirós";

        // Act & SUT
        AuthorId authorId = new AuthorId(name);

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
    void rejectNullNameAuthor() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Author(null));
    }

    @Test
    void testEqualsWithDifferentObjectTypes() {

        // Arrange & SUT
        Author a = new Author("Seneca");
        String b = "Seneca";
        Author b2 = null;

        // Act & Assert
        assertFalse(a.equals(b));
        assertFalse(a.equals(b2));

    }


    @Test
    void testEqualsWithSameObject() {

        // Arrange & SUT
        Author a = new Author("Seneca");

        // Act & Assert
        assertTrue(a.equals(a));

    }

    @Test
    void testEqualsWithDifferentAuthorObjectsSameName() {

        // Arrange & SUT
        Author a = new Author("Seneca");
        Author b = new Author("SeNeca");

        // Act & Assert
        assertFalse(a.equals(b));

    }

    @Test
    void testEqualsWithDifferentAuthorObjectsDifferentName() {

        // Arrange & SUT
        Author a = new Author("Seneca");
        Author b = new Author("Justinian");

        // Act & Assert
        assertFalse(a.equals(b));

    }

    @Test
    void test_equal_hash_code() {

        // Arrange & SUT
        Author a = new Author("Seneca");
        Author a2 = new Author("SeneCA");

        // Act & Assert
        assertEquals(a.hashCode(), a2.hashCode());

    }

    @Test
    void test_non_equal_hash_code() {

        // Arrange & SUT
        Author a = new Author("Seneca");
        Author a2 = new Author("SeneCAR");

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
    void equalsShouldReturnFalseForDifferentAuthorObjectsWithSameName() {

        // Arrange & SUT
        Author a1 = new Author("Seneca");
        Author a2 = new Author("Seneca");

        // Act & Assert
        assertFalse(a1.equals(a2));

    }

    @Test
    void equalsShouldDependOnAuthorIdNotObjectReference() {

        // Arrange & SUT
        Author a1 = new Author("Seneca");
        Author a2 = new Author("Seneca");

        // Act & Assert
        assertNotSame(a1, a2);
        assertFalse(a1.equals(a2));

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

    @Test
    void equalsShouldBeFalseForDifferentInstancesEvenSameName() {

        Author a1 = new Author("Seneca");
        Author a2 = new Author("Seneca");

        assertFalse(a1.equals(a2));
    }

}