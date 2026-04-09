package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryNameTest {

    @Test
    void constructsAndNormalizesNameToUppercase() {
        CountryName name = new CountryName("   Portugal   ");
        assertEquals("PORTUGAL", name.toString());
    }

    @Test
    void acceptsHyphenAndApostropheNamesAndNormalizesToUppercase() {
        CountryName guineaBissau = new CountryName("Guinea-Bissau");
        CountryName coteDIvoire = new CountryName("Cote d'Ivoire");

        assertEquals("GUINEA-BISSAU", guineaBissau.toString());
        assertEquals("COTE D'IVOIRE", coteDIvoire.toString());
    }

    @Test
    void hasConsistentEqualityAndHashCode() {
        CountryName portugal = new CountryName("Portugal");
        CountryName samePortugal = new CountryName("  portugal  ");
        CountryName spain = new CountryName("Spain");

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
        CountryName name = new CountryName("United    Kingdom");
        assertEquals("UNITED KINGDOM", name.toString());
    }

    @Test
    void throwsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> new CountryName(null));
    }

    @Test
    void throwsOnEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new CountryName("   "));
    }

    @Test
    void throwsOnInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> new CountryName("#Portugal"));
        assertThrows(IllegalArgumentException.class, () -> new CountryName("Portugal123"));
    }
}