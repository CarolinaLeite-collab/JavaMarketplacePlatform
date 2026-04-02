package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublishingCompanyIdTest {

    @Test
    void constructorValidNameCreatesPublishingCompanyId() {
        // Arrange
        String name = "Penguin Random House";

        // Act
        PublishingCompanyId publicationTypeId = new PublishingCompanyId(name);

        // Assert
        assertNotNull(publicationTypeId);
    }

    @Test
    void constructorNullNameThrowsIllegalArgumentException() {
        // Arrange
        String name = null;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new PublishingCompanyId(name));

        // Assert
        assertEquals(exception.getMessage(), "PublishingCompanyId cannot be null or blank");
    }

    @Test
    void constructorBlankNameThrowsIllegalArgumentException() {
        // Arrange
        String name = "   ";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new PublishingCompanyId(name));

        // Assert
        assertEquals(exception.getMessage(), "PublishingCompanyId cannot be null or blank");
    }

    @Test
    void constructorEmptyNameThrowsIllegalArgumentException() {
        // Arrange
        String name = "";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new PublishingCompanyId(name));

        // Assert
        assertEquals(exception.getMessage(), "PublishingCompanyId cannot be null or blank");
    }

    @Test
    void equalsSameNameCaseInsensitiveReturnsTrue() {
        // Arrange
        PublishingCompanyId publishingCompanyId1 = new PublishingCompanyId("pENGUIN");

        // Act
        PublishingCompanyId publishingCompanyId2 = new PublishingCompanyId("Penguin");

        // Assert
        assertEquals(publishingCompanyId1, publishingCompanyId2);
    }

    @Test
    void equalsDifferentNameReturnsFalse() {
        // Arrange
        PublishingCompanyId publishingCompanyId1 = new PublishingCompanyId("pENGUIN");

        // Act
        PublishingCompanyId publishingCompanyId2 = new PublishingCompanyId("Simon & Schuster");


        // Assert
        assertNotEquals(publishingCompanyId1, publishingCompanyId2);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // Arrange
        PublishingCompanyId publishingCompanyId = new PublishingCompanyId("Porto Editora");

        // Act
        boolean isEqual = publishingCompanyId.equals(publishingCompanyId);

        // Assert
        assertTrue(isEqual);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        PublishingCompanyId publishingCompanyId = new PublishingCompanyId("Pendant Publishing");

        // Act
        boolean isNull = publishingCompanyId.equals(null);

        // Assert
        assertFalse(isNull);
    }

    @Test
    void hashCodeSameNameReturnsSameHash() {
        // Arrange
        PublishingCompanyId publishingCompanyId1 = new PublishingCompanyId("Penguin");

        // Act
        PublishingCompanyId publishingCompanyId2 = new PublishingCompanyId("PENGUIN");

        // Assert
        assertEquals(publishingCompanyId1.hashCode(), publishingCompanyId2.hashCode());
    }

    @Test
    void toStringReturnsNormalisedName() {
        // Arrange
        String name = " PEnguIN RANdom houSE   ";

        // Act
        PublishingCompanyId publishingCompanyId = new PublishingCompanyId(name);

        // Assert
        assertEquals("PENGUIN RANDOM HOUSE", publishingCompanyId.toString());
    }

    @Test
    void equalsDifferentTypeReturnsFalse() {
        PublishingCompanyId id = new PublishingCompanyId("Penguin");
        assertNotEquals(id, "Penguin");
    }

}