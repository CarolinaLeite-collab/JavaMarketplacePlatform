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

    // Additional mutation-focused tests added below

    @Test
    void allowsMinimumAndMaximumLengths() {
        PhonePrefix prefix = new PhonePrefix("+9");
        Phone min = new Phone(prefix, "1234");
        assertEquals("1234", min.getNationalNumber());
        assertEquals("+91234", min.getE164());

        Phone max = new Phone(prefix, "123456789012");
        assertEquals("123456789012", max.getNationalNumber());
        assertEquals("+9123456789012", max.getE164());
    }

    @Test
    void rejectsBlankAfterCleaning() {
        PhonePrefix prefix = new PhonePrefix("+1");
        // characters removed by replaceAll, resulting in empty string
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, " ( ) "));
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, "   "));
    }

    @Test
    void toStringIsE164AndEqualsContract() {
        PhonePrefix prefix = new PhonePrefix("+123");
        Phone p = new Phone(prefix, "(000) 111-2222");
        assertEquals(p.getE164(), p.toString());

        // equals/hashCode contract with null and different class
        assertNotEquals(null, p);
        assertNotEquals("not-a-phone", p);
    }

    @Test
    void hashCodeMatchesObjectsHashAndVaries() {
        PhonePrefix prefix = new PhonePrefix("+1");
        Phone a = new Phone(prefix, "1234");
        Phone b = new Phone(new PhonePrefix("1"), "12345");

        assertEquals(java.util.Objects.hash(a.getPrefix(), a.getNationalNumber()), a.hashCode());
        assertEquals(java.util.Objects.hash(b.getPrefix(), b.getNationalNumber()), b.hashCode());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalityAndNullDifferentTypeBehavior() {
        PhonePrefix prefix = new PhonePrefix("+1");
        Phone p = new Phone(prefix, "1234");

        // identity and null/different-type behavior
        // different instance with same state should be equal
        assertEquals(p, new Phone(prefix, "1234"));
        assertNotEquals(null, p);
        assertNotEquals("not-a-phone", p);
    }

    @Test
    void cleansTabsAndOtherWhitespaceFromNumber() {
        PhonePrefix prefix = new PhonePrefix("+1");
        Phone p = new Phone(prefix, "12\t34");
        assertEquals("1234", p.getNationalNumber());
        assertEquals("+11234", p.getE164());
    }

    @Test
    void inequalityForDifferentNumbersEvenIfPrefixSame() {
        PhonePrefix prefix = new PhonePrefix("+1");
        Phone a = new Phone(prefix, "1234");
        Phone b = new Phone(prefix, "1235");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void directEqualsIdentityNullAndDifferentType() {
        PhonePrefix prefix = new PhonePrefix("+1");
        Phone p = new Phone(prefix, "4444");

        // direct equals calls to hit the early-return and instanceof branches
        assertTrue(p.equals(p));
        assertFalse(p.equals(null));
        assertFalse(p.equals(new Object()));
    }
}