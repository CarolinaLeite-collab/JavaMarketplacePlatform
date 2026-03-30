package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIDTest {

    @Test
    void constructorShouldThrowWhenEmailIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new UserID(null));
    }

    @Test
    void twoUserIDsWithSameEmailShouldBeEqual() {

        // Arrange
        Email email = new Email("pedro@mail.com");

        UserID id1 = new UserID(email);
        UserID id2 = new UserID(email);

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void twoUserIDsWithDifferentEmailsShouldNotBeEqual() {

        // Arrange
        UserID id1 = new UserID(new Email("pedro@mail.com"));
        UserID id2 = new UserID(new Email("ana@mail.com"));

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalUserIDsShouldHaveSameHashCode() {

        // Arrange
        Email email = new Email("pedro@mail.com");
        UserID id1 = new UserID(email);
        UserID id2 = new UserID(email);

        //Act
        int hash1 = id1.hashCode();
        int hash2 = id2.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void getEmailShouldReturnCorrectEmail() {

        // Arrange
        Email email = new Email("pedro@mail.com");
        UserID id = new UserID(email);

        // Act
        Email result = id.getEmail();

        // Assert
        assertEquals(email, result);
    }

    @Test
    void differentUserIDsShouldHaveDifferentHashCode() {

        // Arrange
        UserID id1 = new UserID(new Email("pedro@mail.com"));
        UserID id2 = new UserID(new Email("ana@mail.com"));

        // Assert
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNotUserID() {

        // Arrange
        UserID id = new UserID(new Email("pedro@mail.com"));
        String notAUserID = "not a user id";

        // Assert
        assertNotEquals(id, notAUserID);
    }

    @Test
    void toStringShouldReturnEmailValue() {

        // Arrange
        Email email = new Email("pedro@mail.com");
        UserID id = new UserID(email);

        // Act
        String result = id.toString();

        // Assert
        assertEquals("pedro@mail.com", result);
    }
}

