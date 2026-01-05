package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhonePrefixTest {

    @Test
    void acceptsPrefixWithPlusOrWithout() {
        PhonePrefix p1 = new PhonePrefix("+351");
        PhonePrefix p2 = new PhonePrefix("351");
        assertEquals("+351", p1.getValue());
        assertEquals(p1, p2);
    }

    @Test
    void trimsWhitespace() {
        PhonePrefix p = new PhonePrefix("  +1 ");
        assertEquals("+1", p.getValue());
    }

    @Test
    void rejectsNullBlankOrTooLong() {
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix(null));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix(" "));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+1234")); // 4 digits
    }

    @Test
    void rejectsNonDigits() {
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+3A1"));
    }
}