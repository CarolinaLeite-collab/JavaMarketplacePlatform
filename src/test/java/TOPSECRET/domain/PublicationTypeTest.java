package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeTest {

    @Test
    void givenValidTypeName_SavesData() {
        PublicationType publicationType = new PublicationType("Hardcover");

        assertEquals("Hardcover", publicationType.getPublicationType());
    }

    @Test
    void givenNullTypeName_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType(null));

        assertEquals("Publication type name is required!", exception.getMessage());
    }

    @Test
    void givenBlankTypeName_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType("   "));

        assertEquals("Publication type name is required!", exception.getMessage());
    }

    @Test
    void givenEmptyTypeName_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType(""));

        assertEquals("Publication type name is required!", exception.getMessage());
    }

    @Test
    void toStringReturnsTypeName() {
        PublicationType publicationType = new PublicationType("Paperback");

        assertEquals("Paperback", publicationType.toString());
    }
}