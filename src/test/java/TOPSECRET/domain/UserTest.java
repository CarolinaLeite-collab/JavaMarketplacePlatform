package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructorWithValidArgumentsCreatesUser() {
        // Arrange + act
        User user = new User(
                new Name("Tiago"),
                new Address("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", Address.Country.PORTUGAL, "4300-111", null),
                new Email("1252008@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "918902632")
        );

        // Assert
        assertEquals("Tiago", user.getName().toString());
        assertEquals("1252008@isep.ipp.pt", user.getEmail().toString());
        assertEquals("PORTUGAL, Matosinhos (Porto), Rua senhor de matosinhos, 81, HOUSE, 4300-111", user.getAddress().toString());
        assertEquals("+351918902632", user.getPhone().toString());
    }

    //Objects.requireNonNull lança NullPointerException (não IllegalArgumentException).
    @Test
    void constructorWithNullName() {
        //Assert
        assertThrows(NullPointerException.class, () ->
                // Arrange + act
                new User(
                        null,
                        new Address("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", Address.Country.PORTUGAL, "4300-111", null),
                        new Email("1252008@isep.ipp.pt"),
                        new Phone(new PhonePrefix("+351"), "918902632")
                )
        );
    }

    @Test
    void constructorWithNullAddress() {
        //Assert
        assertThrows(NullPointerException.class, () ->
                // Arrange + act
                new User(
                        new Name("Tiago"),
                        null,
                        new Email("1252008@isep.ipp.pt"),
                        new Phone(new PhonePrefix("+351"), "918902632")
                )
        );
    }

    @Test
    void constructorWithNullEmail() {
        //Assert
        assertThrows(NullPointerException.class, () ->
                // Arrange + act
                new User(
                        new Name("Tiago"),
                        new Address("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", Address.Country.PORTUGAL, "4300-111", null),
                        null,
                        new Phone(new PhonePrefix("+351"), "918902632")
                )
        );
    }

    @Test
    void constructorWithNullPhoneNumber() {
        //Assert
        assertThrows(NullPointerException.class, () ->
                // Arrange + act
                new User(
                        new Name("Tiago"),
                        new Address("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", Address.Country.PORTUGAL, "4300-111", null),
                        new Email("1252008@isep.ipp.pt"),
                        null
                )
        );
    }

    @Test
    void constructorWithNameAndEmail() {

        // Arrange + Act
        User user = new User(
                new Name("Tiago"),
                new Email("test@email.pt")
        );

        // Assert
        assertEquals("Tiago", user.getName().toString());
        assertEquals("test@email.pt", user.getEmail().toString());
        assertNull(user.getAddress());
        assertNull(user.getPhone());
    }

    @Test
    void test_toString() {

        User t = new User(
                new Name("Tiago"),
                new Email("test@email.pt")
        );

        User t2 = new User(
                new Name("Tiago"),
                new Email("test@email.pt")
        );

        assertEquals(t.toString(), t2.toString());

    }

    @Test
    void test_equals() {

        User u1 = new User(
                new Name("Tiago"),
                new Email("test@email.pt")
        );

        User u2 = new User(
                new Name("Alfredo"),
                new Email("test@email.pt")
        );

        User u3 = new User(
                new Name("Magalhaes"),
                new Email("test2@email.pt")
        );

        String u4 = "user";

        assertEquals(u1, u1);
        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertNotEquals(u1, null);
        assertNotEquals(u1, u4);

    }

    @Test
    void test_hash_code_same_email() {

        User t = new User(
                new Name("Tiago"),
                new Email("test@email.pt")
        );

        User t2 = new User(
                new Name("Alfredo"),
                new Email("TEST@EMAIL.PT")
        );

        assertEquals(t.hashCode(), t2.hashCode());

    }

    @Test
    void test_hash_code_different_email() {

        User t = new User(
                new Name("Tiago"),
                new Email("test@email.pt")
        );

        User t2 = new User(
                new Name("Alfredo"),
                new Email("alfredo@EMAIL.PT")
        );

        assertNotEquals(t.hashCode(), t2.hashCode());

    }


}