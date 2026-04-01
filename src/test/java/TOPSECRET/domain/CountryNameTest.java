package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.CountryName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryNameTest {

    @Test
    void constructsAndNormalizesName() {
        CountryName name = new CountryName("   Portugal   ");
        assertEquals("PORTUGAL", name.value());
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
    }
}

