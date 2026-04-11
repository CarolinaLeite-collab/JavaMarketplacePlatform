package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorIdTest {

    @Test
    void shouldConstructAuthorId() {

        // Act & SUT
        AuthorId id = new AuthorId("Lev Nikoláievitch Tolstói");

    }

    @Test
    void shouldThrowExceptionIfNameIsNull() {

        // Arrange
        String fullName = null;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new AuthorId(fullName));

        // Assert
        assertEquals("AuthorId cannot be null or blank", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionIfNameIsBlank() {

        // Arrange
        String fullName = "   ";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new AuthorId(fullName));

        // Assert
        assertEquals("AuthorId cannot be null or blank", exception.getMessage());

    }

    @Test
    void shouldGenerateCorrectInitialsForFullName() {
        // Arrange
        String name = "Lev Nikoláievitch Tolstói";

        // Act & SUT
        AuthorId id = new AuthorId(name);
        String generated = id.toString();

        // Assert
        assertTrue(generated.startsWith("Tolstói L.N."));
        assertEquals("Tolstói L.N.".length() + 1 + 6, generated.length());
    }

    @Test
    void shouldBeEqualToItself() {

        // Arrange
        String fullName = "Lev Nikoláievitch Tolstói";
        AuthorId id = new AuthorId(fullName);

        // Act
        boolean result = id.equals(id);

        // Assert
        assertTrue(result);

    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        // Arrange
        AuthorId id = new AuthorId("Lev Nikoláievitch Tolstói");

        // Act
        boolean result = id.equals(null);

        // Assert
        assertFalse(result);

    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentType() {

        // Arrange
        String fullName = "Lev Nikoláievitch Tolstói";
        AuthorId id = new AuthorId(fullName);
        String notAnId = "Tolstói.L.N-XXXXXX";

        // Act
        boolean result = id.equals(notAnId);

        // Assert
        assertFalse(result);

    }

    @Test
    void shouldReturnConsistentHashCode() {

        // Arrange
        String fullName = "Lev Nikoláievitch Tolstói";
        AuthorId id = new AuthorId(fullName);

        // Act
        int hash1 = id.hashCode();
        int hash2 = id.hashCode();

        // Assert
        assertEquals(hash1, hash2);

    }

    @Test
    void shouldGenerateDifferentIdsForSameName() {

        // Arrange
        String fullName = "Lev Nikoláievitch Tolstói";

        // Act & SUT
        AuthorId id1 = new AuthorId(fullName);
        AuthorId id2 = new AuthorId(fullName);

        // Assert
        assertNotEquals(id1.toString(), id2.toString());

    }

    @Test
    void shouldEqualObjectsHaveSameHashCode() {

        // Arrange
        String fullName = "Lev Nikoláievitch Tolstói";
        AuthorId id = new AuthorId(fullName);

        // Act
        int hash1 = id.hashCode();
        int hash2 = id.hashCode();

        // Assert
        assertEquals(hash1, hash2);

    }

    @Test
    void shouldReturnFalseForDifferentObjects() {

        // Arrange
        AuthorId id1 = new AuthorId("Lev Nikoláievitch Tolstói");
        AuthorId id2 = new AuthorId("Masaoka Shiki");

        // Act
        boolean result = id1.equals(id2);

        // Assert
        assertFalse(result);

    }

    @Test
    void shouldHandleSingleWordName() {

        // Arrange
        String name = "Shiki";

        // Act & SUT
        AuthorId id = new AuthorId(name);

        // Assert
        assertTrue(id.toString().startsWith("Shiki -"));

    }

    @Test
    void equalObjectsMustHaveSameHashCode() {

        // Arrange
        AppraisalEntityId id1 = new AppraisalEntityId("Lev Nikoláievitch Tolstói");
        AppraisalEntityId id2 = new AppraisalEntityId("Lev Nikoláievitch Tolstói");

        // Act
        int hash1 = id1.hashCode();
        int hash2 = id2.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldNotBeEqualForSameName() {

        // Arrange
        String name = "Lev Nikoláievitch Tolstói";

        // Act
        AuthorId id1 = new AuthorId(name);
        AuthorId id2 = new AuthorId(name);

        // Assert
        assertFalse(id1.equals(id2));

    }

    @Test
    void shouldReturnFalseWhenIdsAreDifferentEvenIfNamesAreSame() {

        // Arrange
        String name = "Lev Nikoláievitch Tolstói";

        // Act
        AuthorId id1 = new AuthorId(name);
        AuthorId id2 = new AuthorId(name);

        // Assert
        assertFalse(id1.equals(id2));

    }

    @Test
    void differentObjectsShouldHaveDifferentHashCodes() {

        // Arrange
        AuthorId id1 = new AuthorId("Lev Tolstói");
        AuthorId id2 = new AuthorId("Masaoka Shiki");

        // Act
        int hash1 = id1.hashCode();
        int hash2 = id2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);

    }

}
