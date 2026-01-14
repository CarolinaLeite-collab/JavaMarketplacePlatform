package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeTest {

    @Test
    void givenValidTypeName_StoresValue() {
        PublicationType publicationType = new PublicationType("Book");

        assertEquals("Book", publicationType.getPublicationType());
    }

    @Test
    void givenNullTypeName_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType(null));

        assertEquals("Publication type name is required!", ex.getMessage());
    }

    @Test
    void givenBlankTypeName_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType("   "));

        assertEquals("Publication type name is required!", ex.getMessage());
    }

    @Test
    void givenEmptyTypeName_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType(""));

        assertEquals("Publication type name is required!", ex.getMessage());
    }
}