package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryNameTest {

    @Test
    void constructsAndNormalizesNameToUppercase() {
        // SUT
        CountryName name = new CountryName("   Portugal   ");
        // Act + Assert
        assertEquals("PORTUGAL", name.toString());
    }

    @Test
    void acceptsHyphenAndApostropheNamesAndNormalizesToUppercase() {
        // SUT
        CountryName guineaBissau = new CountryName("Guinea-Bissau");
        CountryName coteDIvoire = new CountryName("Cote d'Ivoire");

        // Act + Assert
        assertEquals("GUINEA-BISSAU", guineaBissau.toString());
        assertEquals("COTE D'IVOIRE", coteDIvoire.toString());
    }

    @Test
    void hasConsistentEqualityAndHashCode() {
        // SUT
        CountryName portugal = new CountryName("Portugal");
        CountryName samePortugal = new CountryName("  portugal  ");
        CountryName spain = new CountryName("Spain");

        // Act + Assert
        assertEquals(portugal, samePortugal);
        assertEquals(portugal.hashCode(), samePortugal.hashCode());
        assertNotEquals(portugal, spain);
        assertEquals(portugal, portugal);
        assertNotEquals(portugal, new Object());
        assertNotEquals(portugal, null);
        assertEquals("PORTUGAL".hashCode(), portugal.hashCode());
    }

    @Test
    void normalizationHandlesMultipleSpaces() {
        // SUT
        CountryName name = new CountryName("United    Kingdom");
        // Act + Assert
        assertEquals("UNITED KINGDOM", name.toString());
    }

    @Test
    void throwsOnNull() {
        // SUT + Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new CountryName(null));
    }

    @Test
    void throwsOnEmpty() {
        // SUT + Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new CountryName("   "));
    }

    @Test
    void throwsOnInvalidCharacters() {
        // SUT + Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new CountryName("#Portugal"));
        assertThrows(IllegalArgumentException.class, () -> new CountryName("Portugal123"));
    }
}