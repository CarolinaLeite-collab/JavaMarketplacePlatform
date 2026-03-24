package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhoneTest {

    private PhonePrefix _prefixDouble;

    @BeforeEach
    void setUp() {
        _prefixDouble = mock(PhonePrefix.class);
        when(_prefixDouble.getValue()).thenReturn("+1");
    }

    @Test
    void shouldCreatePhoneNumber() {

        new Phone(_prefixDouble,"912-345-678");
    }

    @Test
    void buildsPhoneAndNormalizesNumber() {
        // Arrange
        when(_prefixDouble.getValue()).thenReturn("+351");

        // Act
        Phone phone = new Phone(_prefixDouble, " 912-345-678 ");

        // Assert
        assertEquals("+351", phone.getPrefix().getValue());
        assertEquals("912345678", phone.getNationalNumber());
        assertEquals("+351912345678", phone.getE164());
    }

    @Test
    void cleansSpacesDashesAndParentheses() {
        // Act
        Phone phone = new Phone(_prefixDouble, "(123) 456-789");

        // Assert
        assertEquals("123456789", phone.getNationalNumber());
        assertEquals("+1123456789", phone.getE164());
    }

    @Test
    void cleansTabsAndOtherWhiteSpaceFromNumber() {
        // Act
        Phone phone = new Phone(_prefixDouble, "12\t34");

        // Assert
        assertEquals("1234", phone.getNationalNumber());
        assertEquals("+11234", phone.getE164());
    }

    @Test
    void rejectsNullsAndInvalidLengths() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Phone(null, "123456"));
        assertThrows(IllegalArgumentException.class, () -> new Phone(_prefixDouble, null));
        assertThrows(IllegalArgumentException.class, () -> new Phone(_prefixDouble, "123"));
        assertThrows(IllegalArgumentException.class, () -> new Phone(_prefixDouble, "1234567890123"));
    }

    @Test
    void rejectsLettersInNumber() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Phone(_prefixDouble, "12A456"));
    }

    @Test
    void allowsMinimumAndMaximumLengths() {
        // Arrange
        when(_prefixDouble.getValue()).thenReturn("+9");

        Phone min = new Phone(_prefixDouble, "1234");
        Phone max = new Phone(_prefixDouble, "123456789012");

        // Assert & Act
        assertEquals("1234", min.getNationalNumber());
        assertEquals("+91234", min.getE164());
        assertEquals("123456789012", max.getNationalNumber());
        assertEquals("+9123456789012", max.getE164());
    }

    @Test
    void rejectsBlankAfterCleaning() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Phone(_prefixDouble, " ( ) "));
        assertThrows(IllegalArgumentException.class, () -> new Phone(_prefixDouble, "   "));
    }

    @Test
    void equalityUsesNormalizedValues() {
        // Arrange
        when(_prefixDouble.getValue()).thenReturn("+44");

        PhonePrefix _prefixDouble2 = mock(PhonePrefix.class);
        when(_prefixDouble2.getValue()).thenReturn("44");

        Phone a = new Phone(_prefixDouble, "123 456");
        Phone b = new Phone(_prefixDouble2, "123456");

        // Assert & Act
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCodeMatchesNormalizedValueAndVaries() {
        // Arrange
        PhonePrefix _prefixDouble2 = mock(PhonePrefix.class);
        when(_prefixDouble2.getValue()).thenReturn("1");

        Phone a = new Phone(_prefixDouble, "1234");
        Phone b = new Phone(_prefixDouble2, "12345");

        String normalizedA = "1" + "1234";
        String normalizedB = "1" + "12345";

        // Assert & Act
        assertEquals(normalizedA.hashCode(), a.hashCode());
        assertEquals(normalizedB.hashCode(), b.hashCode());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalityAndNullDifferentTypeBehavior() {
        // Arrange
        Phone p = new Phone(_prefixDouble, "1234");

        // Assert & Act
        assertEquals(p, new Phone(_prefixDouble, "1234"));
        assertNotEquals(null, p);
        assertNotEquals("not-a-phone", p);
    }

    @Test
    void inequalityForDifferentNumbersEvenIfPrefixSame() {
        // Arrange
        Phone a = new Phone(_prefixDouble, "1234");
        Phone b = new Phone(_prefixDouble, "1235");

        // Assert & Act
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void directEqualsIdentityNullAndDifferentType() {
        // Arrange
        Phone p = new Phone(_prefixDouble, "4444");

        // Assert & Act
        assertTrue(p.equals(p));
        assertFalse(p.equals(null));
        assertFalse(p.equals(new Object()));
    }

    @Test
    void toStringIsE164AndEqualsContract() {
        // Arrange
        Phone p = new Phone(_prefixDouble, "(000) 111-2222");

        // Assert
        assertEquals(p.getE164(), p.toString());
    }
}


