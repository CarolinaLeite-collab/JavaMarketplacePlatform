package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeIdTest {

    @Test
    void constructorValidNameCreatesPublicationTypeId() {
        // Arrange
        String name = "Book";

        // Act
        PublicationTypeId publicationTypeId = new PublicationTypeId(name);

        // Assert
        assertNotNull(publicationTypeId);
    }

    @Test
    void constructorNullNameThrowsIllegalArgumentException() {
        // Arrange
        String name = null;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new PublicationTypeId(name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorBlankNameThrowsIllegalArgumentException() {
        // Arrange
        String name = "   ";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new PublicationTypeId(name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorEmptyNameThrowsIllegalArgumentException() {
        // Arrange
        String name = "";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new PublicationTypeId(name));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void constructorNormalisesNameToUpperCase() {
        // Arrange
        String name = "magazine";

        // Act
        PublicationTypeId publicationTypeId = new PublicationTypeId(name);

        // Assert
        assertEquals("MAGAZINE", publicationTypeId.toString());
    }

    @Test
    void equalsSameNameCaseInsensitiveReturnsTrue() {
        // Arrange
        PublicationTypeId publicationTypeId1 = new PublicationTypeId("Book");

        // Act
        PublicationTypeId publicationTypeId2 = new PublicationTypeId("book");

        // Assert
        assertEquals(publicationTypeId1, publicationTypeId2);
    }

    @Test
    void equalsDifferentNameReturnsFalse() {
        // Arrange
        PublicationTypeId publicationTypeId1 = new PublicationTypeId("Book");

        // Act
        PublicationTypeId publicationTypeId2 = new PublicationTypeId("Magazine");

        // Assert
        assertNotEquals(publicationTypeId1, publicationTypeId2);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // Arrange
        PublicationTypeId publicationTypeId = new PublicationTypeId("Book");

        // Act
        boolean isEqual = publicationTypeId.equals(publicationTypeId);

        // Assert
        assertTrue(isEqual);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        PublicationTypeId publicationTypeId = new PublicationTypeId("Book");

        // Act
        boolean isNull = publicationTypeId.equals(null);

        // Assert
        assertFalse(isNull);
    }

    @Test
    void hashCodeSameNameReturnsSameHash() {
        // Arrange
        PublicationTypeId publicationTypeId1 = new PublicationTypeId("Book");

        // Act
        PublicationTypeId publicationTypeId2 = new PublicationTypeId("BOOk");

        // Assert
        assertEquals(publicationTypeId1.hashCode(), publicationTypeId2.hashCode());
    }

    @Test
    void toStringReturnsNormalisedName() {
        // Arrange
        String name = "  mAgAzInE ";

        // Act
        PublicationTypeId publicationTypeId = new PublicationTypeId(name);

        // Assert
        assertEquals("MAGAZINE", publicationTypeId.toString());
    }

}