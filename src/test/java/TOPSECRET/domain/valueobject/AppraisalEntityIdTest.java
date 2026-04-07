package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppraisalEntityIdTest {

    @Test
    void shouldConstructAppraisalEntityId() {

        // Arrange
        String fullName = "Rembrandt Harmenszoon van Rijn";

        // Act
        AppraisalEntityId id = new AppraisalEntityId(fullName);

    }

    @Test
    void shouldThrowExceptionIfNameIsNull() {

        // Arrange
        String fullName = null;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new AppraisalEntityId(fullName));

        // Assert
        assertEquals("AppraisalEntityId cannot be null or blank", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionIfNameIsBlank() {

        // Arrange
        String fullName = "   ";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new AppraisalEntityId(fullName));

        // Assert
        assertEquals("AppraisalEntityId cannot be null or blank", exception.getMessage());

    }

    @Test
    void shouldGenerateCorrectInitialsForFullName() {
        // Arrange
        String name = "Rembrandt Harmenszoon van Rijn";

        // Act
        AppraisalEntityId id = new AppraisalEntityId(name);
        String generated = id.toString();

        // Assert
        assertTrue(generated.startsWith("RijnR.H.V."));
        assertEquals("RijnR.H.V.".length() + 1 + 6, generated.length());
    }

    @Test
    void shouldBeEqualToItself() {

        // Arrange
        String fullName = "Rembrandt Harmenszoon van Rijn";
        AppraisalEntityId id = new AppraisalEntityId(fullName);

        // Act
        boolean result = id.equals(id);

        // Assert
        assertTrue(result);

    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        // Arrange
        AppraisalEntityId id = new AppraisalEntityId("Rembrandt Harmenszoon van Rijn");

        // Act
        boolean result = id.equals(null);

        // Assert
        assertFalse(result);

    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentType() {

        // Arrange
        String fullName = "Rembrandt Harmenszoon van Rijn";
        AppraisalEntityId id = new AppraisalEntityId(fullName);
        String notAnId = "Rijn.R.H.V-XXXXXX";

        // Act
        boolean result = id.equals(notAnId);

        // Assert
        assertFalse(result);

    }

    @Test
    void shouldReturnConsistentHashCode() {

        // Arrange
        String fullName = "Rembrandt Harmenszoon van Rijn";
        AppraisalEntityId id = new AppraisalEntityId(fullName);

        // Act
        int hash1 = id.hashCode();
        int hash2 = id.hashCode();

        // Assert
        assertEquals(hash1, hash2);

    }

    @Test
    void shouldGenerateDifferentIdsForSameName() {

        // Arrange
        String fullName = "Rembrandt Harmenszoon van Rijn";

        // Act
        AppraisalEntityId id1 = new AppraisalEntityId(fullName);
        AppraisalEntityId id2 = new AppraisalEntityId(fullName);

        // Assert
        assertNotEquals(id1.toString(), id2.toString());

    }

    @Test
    void shouldEqualObjectsHaveSameHashCode() {

        // Arrange
        String fullName = "Rembrandt Harmenszoon van Rijn";
        AppraisalEntityId id = new AppraisalEntityId(fullName);

        // Act
        int hash1 = id.hashCode();
        int hash2 = id.hashCode();

        // Assert
        assertEquals(hash1, hash2);

    }

    @Test
    void shouldReturnFalseForDifferentObjects() {

        // Arrange
        AppraisalEntityId id1 = new AppraisalEntityId("Rembrandt Harmenszoon van Rijn");
        AppraisalEntityId id2 = new AppraisalEntityId("Caspar David Friedrich");

        // Act
        boolean result = id1.equals(id2);

        // Assert
        assertFalse(result);

    }

    @Test
    void shouldHandleSingleWordName() {
        // Arrange
        String name = "Rembrandt";

        // Act
        AppraisalEntityId id = new AppraisalEntityId(name);

        // Assert
        assertTrue(id.toString().startsWith("Rembrandt-"));
    }

}
