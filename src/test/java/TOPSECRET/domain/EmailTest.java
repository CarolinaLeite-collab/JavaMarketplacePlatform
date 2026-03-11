package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void constructorShouldBuildEmail() {

        // Arrange
        String email1= "test@testing.com";

        // Act
        Email email = new Email (email1);

        // Assert
        assertNotNull(email);
    }

    @Test
    void testInvalidEmailFormat() {

        // Arrange
        String email1 = "test123.com";
        String email2 = "@testing.com";
        String email3 = "I/am/testing@123.com";
        String email4 = "how@about@this.com";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Email(email1));
        assertThrows(IllegalArgumentException.class, () -> new Email(email2));
        assertThrows(IllegalArgumentException.class, () -> new Email(email3));
        assertThrows(IllegalArgumentException.class, () -> new Email(email4));

    }

    @Test
    void constructorShouldThrowWhenEmailIsNull() {

        // Arrange
        String email1 = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Email(email1));
    }

    @Test
    void constructorShouldThrowWhenEmailIsEmpty() {

        // Arrange
        String email = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Email(email));
    }

    @Test
    void constructorShouldThrowWhenEmailIsBlank() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Email("   "));
    }

    @Test
    void constructorShouldNormaliseEmailToLowercase() {

        // Arrange
        String email1 = "TEST@SOMETHING.COM";
        String email2 = " TEST@SOMETHING.COM ";
        Email emailTest = new Email(email1);
        Email email2Test = new Email(email2);

        // Act
        String emailToTest = emailTest.getValue();
        String emailToTest2 = email2Test.getValue();

        // Assert
        assertEquals(emailToTest, "test@something.com");
        assertEquals(emailToTest2, "test@something.com");

    }

    @Test
    void equalsShouldReturnTrueForSameObject() {

        // Arrange
        Email email = new Email("test@testing.com");

        // Act
        boolean result = email.equals(email);

        // Assert
        assertTrue(result);
    }

    @Test
    void testEqualsShouldReturnFalseForNullObject() {

        // Arrange & Act
        Email email = new Email("test@testing.com");

        // Assert
        assertNotEquals(email, null);
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {

        // Arrange & Act
        Email email = new Email("test@testing.com");
        String differentClass = "differentClass";

        // Assert
        assertNotEquals(email, differentClass);
    }

    @Test
    void equalsShouldReturnFalseForDifferentEmails() {

        // Arrange & Act
        Email email1 = new Email("test1@testing.com");
        Email email2 = new Email("test2@testing.com");

        // Assert
        assertNotEquals(email1, email2);
    }

    @Test
    void equalsShouldReturnTrueForSameEmailWithDifferentCase() {

        // Arrange
        String email1 = "IamAeMaIL@TESting.COM";
        String email2 = "iAMaEmAil@tesTING.com";

        // Act
        Email emailTest1 = new Email(email1);
        Email emailTest2 = new Email(email2);

        // Assert
        assertEquals(emailTest1, emailTest2);
    }

    @Test
    void toStringShouldReturnLowercaseEmail() {

        // Arrange
        String email1 = "testing@123.com";
        String email2 = "TESTING@123.com";

        String email3 = "test@testing.com";
        String email4 = "test@testing.com";

        // Act
        Email emailTest1 = new Email(email2);
        Email emailTest2 = new Email(email3);

        // Assert
        assertEquals(email1, emailTest1.toString());
        assertEquals(email4, emailTest2.toString());

    }

    // Test hash equals method
    @Test
    void hashCodeShouldBeDifferentForDifferentEmails() {

        // Arrange & Act
        Email email1 = new Email("test1@testing.com");
        Email email2 = new Email("test2@testing.com");

        // Assert
        assertNotEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void hashCodeShouldBeEqualForSameEmail() {

        // Arrange
        String email1 = "IamAeMaIL@TESting.COM";
        String email2 = "iAMaEmAil@tesTING.com";

        // Act
        Email emailTest1 = new Email(email1);
        Email emailTest2 = new Email(email2);

        // Assert
        assertEquals(emailTest1.hashCode(), emailTest2.hashCode());
    }
}