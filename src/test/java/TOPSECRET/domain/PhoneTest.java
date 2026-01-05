package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    @Test
    void buildsPhoneAndNormalizesNumber() {
        Phone phone = new Phone(new PhonePrefix("+351"), " 912-345-678 ");
        assertEquals("+351", phone.getPrefix().getValue());
        assertEquals("912345678", phone.getNationalNumber());
        assertEquals("+351912345678", phone.getE164());
    }

    @Test
    void rejectsNullsAndInvalidLengths() {
        PhonePrefix prefix = new PhonePrefix("+1");
        assertThrows(IllegalArgumentException.class, () -> new Phone(null, "123456"));
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, null));
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, "123"));      // too short
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, "1234567890123")); // too long (13 digits)
    }

    @Test
    void rejectsLettersInNumber() {
        PhonePrefix prefix = new PhonePrefix("+44");
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, "12A456"));
    }

    @Test
    void equalityUsesNormalizedValues() {
        PhonePrefix p1 = new PhonePrefix("+44");
        PhonePrefix p2 = new PhonePrefix("44");
        Phone a = new Phone(p1, "123 456");
        Phone b = new Phone(p2, "123456");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}