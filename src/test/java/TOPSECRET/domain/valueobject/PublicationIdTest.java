package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PublicationIdTest {

    @Test
    void constructorValidIdCreatesPublicationId() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";

        // Act
        PublicationId publicationId = new PublicationId(id); // SUT

        // Assert
        assertNotNull(publicationId);
    }

    @Test
    void constructorNullIdThrowsIllegalArgumentException() {
        // Arrange
        String id = null;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new PublicationId(id)); // SUT

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorBlankIdThrowsIllegalArgumentException() {
        // Arrange
        String id = "   ";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new PublicationId(id)); // SUT

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorEmptyIdThrowsIllegalArgumentException() {
        // Arrange
        String id = "";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new PublicationId(id)); // SUT

        // Assert
        assertNotNull(exception);
    }

    @Test
    void equalsSameIdReturnsTrue() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";
        PublicationId publicationId1 = new PublicationId(id);

        // Act
        PublicationId publicationId2 = new PublicationId(id); // SUT

        // Assert
        assertEquals(publicationId1, publicationId2);
    }

    @Test
    void equalsDifferentIdReturnsFalse() {
        // Arrange
        PublicationId publicationId1 = new PublicationId("id-one");

        // Act
        PublicationId publicationId2 = new PublicationId("id-two"); // SUT

        // Assert
        assertNotEquals(publicationId1, publicationId2);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // Arrange
        PublicationId publicationId = new PublicationId("id-one");

        // Act
        boolean result = publicationId.equals(publicationId); // SUT

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        PublicationId publicationId = new PublicationId("id-one");

        // Act
        boolean result = publicationId.equals(null); // SUT

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameIdReturnsSameHash() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";
        PublicationId publicationId1 = new PublicationId(id);

        // Act
        PublicationId publicationId2 = new PublicationId(id); // SUT

        // Assert
        assertEquals(publicationId1.hashCode(), publicationId2.hashCode());
    }

    @Test
    void toStringReturnsId() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";

        // Act
        PublicationId publicationId = new PublicationId(id); // SUT

        // Assert
        assertEquals(id, publicationId.toString());
    }

}