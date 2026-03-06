package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhoneTest {

    /**
     * Construction and Normalization
     */
    @Test
    void buildsPhoneAndNormalizesNumber() {
        // Arrange
        PhonePrefix prefix = mock(PhonePrefix.class);
        when(prefix.getValue()).thenReturn("+351");

        // Act
        Phone phone = new Phone(prefix, " 912-345-678 ");

        // Assert
        assertEquals("+351", phone.getPrefix().getValue());
        assertEquals("912345678", phone.getNationalNumber());
        assertEquals("+351912345678", phone.getE164());
    }

    @Test
    void cleansSpacesDashesAndParentheses() {

        PhonePrefix prefix = mock(PhonePrefix.class);
        when(prefix.getValue()).thenReturn("+1");

        Phone phone = new Phone(prefix, "(123) 456-789");

        assertEquals("123456789", phone.getNationalNumber());
        assertEquals("+1123456789", phone.getE164());
    }

    @Test
    void cleansTabsAndOtherWhiteSpaceFromNumber() {

        PhonePrefix prefix = mock(PhonePrefix.class);
        when(prefix.getValue()).thenReturn("+1");

        Phone phone = new Phone(prefix, "12\t34");

        assertEquals("1234", phone.getNationalNumber());
        assertEquals("+11234", phone.getE164());
    }

    /**
     * Validation
     */
    @Test
    void rejectsNullsAndInvalidLengths() {
        // Arrange
        PhonePrefix prefix = mock(PhonePrefix.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Phone(null, "123456"));
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, null));
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, "123"));      // too short
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, "1234567890123")); // too long (13 digits)
    }

    @Test
    void rejectsLettersInNumber() {
        // Arrange
        PhonePrefix prefix = mock(PhonePrefix.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, "12A456"));
    }

    @Test
    void allowsMinimumAndMaximumLengths() {
        // Arrange
        PhonePrefix prefix = mock(PhonePrefix.class);
        when(prefix.getValue()).thenReturn("+9");

        // Act
        Phone min = new Phone(prefix, "1234");
        Phone max = new Phone(prefix, "123456789012");

        // Assert
        assertEquals("1234", min.getNationalNumber());
        assertEquals("+91234", min.getE164());
        assertEquals("123456789012", max.getNationalNumber());
        assertEquals("+9123456789012", max.getE164());
    }

    @Test
    void rejectsBlankAfterCleaning() {
        // Arrange
        PhonePrefix prefix = mock(PhonePrefix.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, " ( ) "));
        assertThrows(IllegalArgumentException.class, () -> new Phone(prefix, "   "));
    }

    /**
     * Equality and Hashcode tests (Not using Mockito)
     * For equality tests, mocks are not appropriate because mack.equals() uses identity.
     * Must use real prefixes
     */
    @Test
    void equalityUsesNormalizedValues() {
        // Arrange
        PhonePrefix p1 = new PhonePrefix("+44");
        PhonePrefix p2 = new PhonePrefix("44");

        Phone a = new Phone(p1, "123 456");
        Phone b = new Phone(p2, "123456");

        // Assert
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCodeMatchesObjectsHashAndVaries() {
        // Arrange
        PhonePrefix prefix = new PhonePrefix("+1");

        Phone a = new Phone(prefix, "1234");
        Phone b = new Phone(new PhonePrefix("1"), "12345");

        // Assert
        assertEquals(java.util.Objects.hash(a.getPrefix(), a.getNationalNumber()), a.hashCode());
        assertEquals(java.util.Objects.hash(b.getPrefix(), b.getNationalNumber()), b.hashCode());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalityAndNullDifferentTypeBehavior() {
        // Arrange
        PhonePrefix prefix = new PhonePrefix("+1");
        Phone p = new Phone(prefix, "1234");

        // Assert
        assertEquals(p, new Phone(prefix, "1234"));
        assertNotEquals(null, p);
        assertNotEquals("not-a-phone", p);
    }

    @Test
    void inequalityForDifferentNumbersEvenIfPrefixSame() {
        // Arrange
        PhonePrefix prefix = new PhonePrefix("+1");
        Phone a = new Phone(prefix, "1234");
        Phone b = new Phone(prefix, "1235");

        // Assert
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void directEqualsIdentityNullAndDifferentType() {
        // Arrange
        PhonePrefix prefix = new PhonePrefix("+1");
        Phone p = new Phone(prefix, "4444");

        // Assert
        assertTrue(p.equals(p));
        assertFalse(p.equals(null));
        assertFalse(p.equals(new Object()));
    }

    /**
     * To String tests
     */
    @Test
    void toStringIsE164AndEqualsContract() {
        // Arrange
        PhonePrefix prefix = mock(PhonePrefix.class);
        when(prefix.getValue()).thenReturn("+123");

        Phone p = new Phone(prefix, "(000) 111-2222");

        // Assert
        assertEquals(p.getE164(), p.toString());
        //assertNotEquals(null, p);
        //assertNotEquals("not-a-phone", p);
    }
}


