package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhonePrefixTest {

    @Test
    void acceptsPrefixWithPlusOrWithout() {
        // Act
        PhonePrefix p1 = new PhonePrefix("+351");
        PhonePrefix p2 = new PhonePrefix("351");

        // Assert
        assertEquals("+351", p1.getValue());
        assertEquals(p1, p2);
    }

    @Test
    void trimsWhitespace() {
        // Act
        PhonePrefix p = new PhonePrefix("  +1 ");

        // Assert
        assertEquals("+1", p.getValue());
    }

    @Test
    void rejectsNullBlankOrTooLong() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix(null));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix(" "));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+1234")); // 4 digits
    }

    @Test
    void rejectsNonDigits() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+3A1"));
    }

    // Additional mutation-focused assertions below

    @Test
    void normalizesVariousInputsAndBorders() {
        // Act & Assert
        assertEquals("+44", new PhonePrefix("+ 44").getValue());
        assertEquals("+00", new PhonePrefix(" 00 ").getValue());
        assertEquals("+9", new PhonePrefix("9").getValue());
        assertEquals("+123", new PhonePrefix("123").getValue());
        assertEquals("+1", new PhonePrefix("  +1  ").getValue());
    }

    @Test
    void acceptsInternalSpacesAndPlusInterDigitSpaces() {
        // Act & Assert
        assertEquals("+12", new PhonePrefix("1 2").getValue());
        assertEquals("+12", new PhonePrefix(" 1 2 ").getValue());
        assertEquals("+12", new PhonePrefix("+1 2").getValue());
    }

    @Test
    void rejectsMultipleLeadingPluses() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("++1"));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("++ 1"));
    }

    @Test
    void equalsHashCodeAndToStringBehavior() {
        // Arrange
        PhonePrefix a = new PhonePrefix("+1");
        PhonePrefix b = new PhonePrefix("  1  ");
        PhonePrefix c = new PhonePrefix("+91");

        // Assert
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals("+1", a.toString());

        assertNotEquals(a, c);
        assertNotEquals(null, a);
        assertNotEquals(new Object(), a);
    }

    @Test
    void rejectsPlusOnlyAndEmbeddedSpaceOverflow() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+"));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+   "));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+12 34"));
    }

    @Test
    void selfEqualityAndHashCodeStability() {
        // Arrange
        PhonePrefix p = new PhonePrefix("+7");

        // Act
        int h1 = p.hashCode();
        int h2 = p.hashCode();

        // Assert
        assertEquals(h1, h2);
    }

    @Test
    void rejectsTabAndNonBreakingSpaceInside() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+1\t2"));
        assertThrows(IllegalArgumentException.class, () -> new PhonePrefix("+1\u00A01"));
    }

    @Test
    void hashCodeMatchesObjectsHashAndVariesAcrossValues() {
        // Arrange
        PhonePrefix a = new PhonePrefix("+1");
        PhonePrefix b = new PhonePrefix("+2");

        // Assert
        assertEquals(java.util.Objects.hash(a.getValue()), a.hashCode());
        assertEquals(java.util.Objects.hash(b.getValue()), b.hashCode());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void selfAndNullAndDifferentTypeEqualsBehavior() {
        // Arrange
        PhonePrefix p = new PhonePrefix("+1");

        // Assert
        assertTrue(p.equals(p));
        assertFalse(p.equals(null));
        assertFalse(p.equals(new Object()));
    }
}