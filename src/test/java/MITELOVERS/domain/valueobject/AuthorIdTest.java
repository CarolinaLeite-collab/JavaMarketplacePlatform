package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorIdTest {

    @Test
    void testConstructAuthorId() {

        // Act & SUT
        AuthorId id = new AuthorId("Lev Nikoláievitch Tolstói");

    }

    @Test
    void shouldPreserveGivenIdValue() {

        // Arrange
        String raw = "Tolstói L.N.-ABC123";

        // Act & SUT
        AuthorId id = new AuthorId(raw);

        // Assert
        assertEquals(raw, id.toString());
        assertEquals(raw.hashCode(), id.hashCode());
    }

    @Test
    void shouldThrowExceptionIfNameIsNull() {

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new AuthorId((Name)null));

        // Assert
        assertEquals("AuthorId cannot be null", exception.getMessage());

    }

    @Test
    void constructorNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AuthorId((String)null));
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
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("Shiki");

        // Act
        AuthorId id = new AuthorId(name);

        // Assert
        assertTrue(id.toString().startsWith("Shiki "));
        assertTrue(id.toString().contains("-"));
    }

    @Test
    void shouldGenerateInitialsFromAllButLastName() {

        // Arrange
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("Lev Nikoláievitch Tolstói");

        // Act
        AuthorId id = new AuthorId(name);

        String value = id.toString();

        // Assert
        assertTrue(value.startsWith("Tolstói "));
        assertTrue(value.contains("L.N."));
        assertFalse(value.contains("T."));
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
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("Lev Nikoláievitch Tolstói");

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

    @Test
    void equalsShouldReturnTrueForSameIdValue() {

        // Arrange
        String idValue = "Tolstói L.N.-ABC123";

        // Act & SUT
        AuthorId id1 = new AuthorId(idValue);
        AuthorId id2 = new AuthorId(idValue);

        // Assert
        assertTrue(id1.equals(id2));
    }
}
