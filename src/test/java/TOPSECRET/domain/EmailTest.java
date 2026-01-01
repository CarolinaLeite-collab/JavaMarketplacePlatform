package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    // Constructor test
    @Test
    void test_a_constructor() {

        // arrange
        String email1= "test@testing.com";

        // act
        Email email = new Email (email1);

    }


    // Test invalid email formats
    @Test
    void test_invalid_email_format() {

        // arrange
        String email1 = "test123.com";
        String email2 = "@testing.com";
        String email3 = "I/am/testing@123.com";
        String email4 = "how@about@this.com";

        // act and assert
        assertThrows(IllegalArgumentException.class, () -> new Email(email1));
        assertThrows(IllegalArgumentException.class, () -> new Email(email2));
        assertThrows(IllegalArgumentException.class, () -> new Email(email3));
        assertThrows(IllegalArgumentException.class, () -> new Email(email4));

    }

    // Test blank or null email formats
    @Test
    void test_blank_or_null_email() {

        // arrange
        String email1 = null;
        String email2 = "";

        // act and assert
        assertThrows(IllegalArgumentException.class, () -> new Email(email1));
        assertThrows(IllegalArgumentException.class, () -> new Email(email2));

    }

    // Test correct email capitalization and extra spaces
    @Test
    void test_email_capitalization() {

        // arrange
        String email1 = "TEST@SOMETHING.COM";
        String email2 = " TEST@SOMETHING.COM ";
        Email email = new Email(email1);
        Email email2 = new Email(email1);

        // act
        String emailToTest = email.getValue();
        String emailToTest2 = email.getValue();

        // assert
        assertEquals(emailToTest, "test@something.com");
        assertEquals(emailToTest2, "test@something.com");

    }

    // Test equals method
    @Test
    void test_equals() {

        // arrange
        String email1 = "IamAeMaIL@TESting.COM";
        String email2 = "iAMaEmAil@tesTING.com";

        // act
        Email emailTest1 = new Email(email1);
        Email emailTest2 = new Email(email2);

        // assert
        assertEquals(emailTest1, emailTest2);

    }

    // Test toString method
    @Test
    void test_toString() {

        String email1 = "testing@123.com";
        String email2 = "TESTING@123.com";

        String email3 = "test@testing.com";
        String email4 = "test@testing.com";

        Email emailTest1 = new Email(email2);
        Email emailTest2 = new Email(email3);

        assertEquals(email1, emailTest1.toString());
        assertEquals(email4, emailTest2.toString());

    }

    // Test hash equals method
    @Test
    void test_hash_equals() {

        // arrange
        String email1 = "IamAeMaIL@TESting.COM";
        String email2 = "iAMaEmAil@tesTING.com";

        // act
        Email emailTest1 = new Email(email1);
        Email emailTest2 = new Email(email2);

        // assert
        assertEquals(emailTest1.hashCode(), emailTest2.hashCode());

    }

}