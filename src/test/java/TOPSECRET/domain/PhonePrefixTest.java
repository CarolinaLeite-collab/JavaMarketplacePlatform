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

    // Additional mutation-focused assertions below

    @Test
    void normalizesVariousInputsAndBorders() {
        // embedded space between plus and digits
        assertEquals("+44", new PhonePrefix("+ 44").getValue());
        // leading zeros preserved
        assertEquals("+00", new PhonePrefix(" 00 ").getValue());
        // 1-digit boundary
        assertEquals("+9", new PhonePrefix("9").getValue());
        // 3-digit boundary
        assertEquals("+123", new PhonePrefix("123").getValue());
        // extra surrounding spaces
        assertEquals("+1", new PhonePrefix("  +1  ").getValue());
    }

    @Test
    void acceptsInternalSpacesAndPlusInterDigitSpaces() {
        // internal spaces without leading plus are removed
        assertEquals("+12", new PhonePrefix("1 2").getValue());
        assertEquals("+12", new PhonePrefix(" 1 2 ").getValue());

        // plus with inter-digit spaces is allowed (spaces removed)
        assertEquals("+12", new PhonePrefix("+1 2").getValue());
    }

    @Test
    void rejectsMultipleLeadingPluses() {
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("++1"));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("++ 1"));
    }

    @Test
    void equalsHashCodeAndToStringBehavior() {
        PhonePrefix a = new PhonePrefix("+1");
        PhonePrefix b = new PhonePrefix("  1  ");
        PhonePrefix c = new PhonePrefix("+91");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals("+1", a.toString());

        assertNotEquals(a, c);
        assertNotEquals(null, a);
        assertNotEquals(new Object(), a);
    }

    @Test
    void rejectsPlusOnlyAndEmbeddedSpaceOverflow() {
        // a string that is just '+' should be rejected (becomes empty after removing plus)
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+"));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+   "));
        // embedded spaces that increase effective digits beyond 3
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+12 34"));
    }

    // New mutation-focused tests
    @Test
    void selfEqualityAndHashCodeStability() {
        PhonePrefix p = new PhonePrefix("+7");
        int h1 = p.hashCode();
        int h2 = p.hashCode();
        assertEquals(h1, h2); // stable hashCode across calls
    }

    @Test
    void rejectsTabAndNonBreakingSpaceInside() {
        // Tab inside should not be removed by replace(" ", "") and thus be invalid
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+1\t2"));
        // Non-breaking space (\u00A0) is not a regular space and thus will cause invalidation
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+1\u00A01"));
    }

    @Test
    void hashCodeMatchesObjectsHashAndVariesAcrossValues() {
        PhonePrefix a = new PhonePrefix("+1");
        PhonePrefix b = new PhonePrefix("+2");

        // Ensure implementation uses Objects.hash(_value) and is not a constant
        assertEquals(java.util.Objects.hash(a.getValue()), a.hashCode());
        assertEquals(java.util.Objects.hash(b.getValue()), b.hashCode());

        // Different logical prefixes should usually produce different hash codes
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void selfAndNullAndDifferentTypeEqualsBehavior() {
        PhonePrefix p = new PhonePrefix("+1");
        // identity should be true
        assertTrue(p.equals(p));
        // null should be handled by instanceof guard and return false
        assertFalse(p.equals(null));
        // different class should return false and exercise instanceof check on this side
        assertFalse(p.equals(new Object()));
    }
}