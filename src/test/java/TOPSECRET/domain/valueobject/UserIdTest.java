package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void constructorShouldThrowWhenEmailIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new UserId(null));
    }

    @Test
    void twoUserIdsWithSameEmailShouldBeEqual() {

        // Arrange
        Email email = new Email("pedro@mail.com");

        UserId id1 = new UserId(email);
        UserId id2 = new UserId(email);

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void twoUserIdsWithDifferentEmailsShouldNotBeEqual() {

        // Arrange
        UserId id1 = new UserId(new Email("pedro@mail.com"));
        UserId id2 = new UserId(new Email("ana@mail.com"));

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalUserIdsShouldHaveSameHashCode() {

        // Arrange
        Email email = new Email("pedro@mail.com");
        UserId id1 = new UserId(email);
        UserId id2 = new UserId(email);

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
        UserId id = new UserId(email);

        // Act
        Email result = id.getEmail();

        // Assert
        assertEquals(email, result);
    }

    @Test
    void differentUserIdsShouldHaveDifferentHashCode() {

        // Arrange
        UserId id1 = new UserId(new Email("pedro@mail.com"));
        UserId id2 = new UserId(new Email("ana@mail.com"));

        // Assert
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNotUserId() {

        // Arrange
        UserId id = new UserId(new Email("pedro@mail.com"));
        String notAUserId = "not a user id";

        // Assert
        assertNotEquals(id, notAUserId);
    }
}

