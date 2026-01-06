package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructorWithValidArgumentsCreatesUser(){
        User user = new User(
                new Name("Tiago"),
                new Email ("1252008@isep.ipp.pt"),
                new Address ("Rua senhor de matosinhos"),
                new Phone( new PhonePrefix("+351"),"918902632")
        );

        assertEquals("Tiago", user.getName());
        assertEquals("1252008@isep.ipp.pt", user.getEmail());
        assertEquals("Rua senhor de matosinhos", user.getAddress());
        assertEquals("+351 918902632", user.getPhone());
    }
//Objects.requireNonNull lança NullPointerException (não IllegalArgumentException).
    @Test
    void constructorWithNullName(){
        assertThrows(NullPointerException.class, () ->
                new User(
                        null,
                    new Address("Rua senhor de matosinhos"),
                    new Email("1252008@isep.ipp.pt"),
                    new Phone( new PhonePrefix("+351"),"918902632")
                )
        );
    }

    @Test
    void constructorWithNullAddress(){
        assertThrows(NullPointerException.class, () ->
                new User(
                        new Name("Tiago"),
                        null,
                        new Email("1252008@isep.ipp.pt"),
                        new Phone( new PhonePrefix("+351"),"918902632")
                )
        );
    }

    @Test
    void constructorWithNullEmail(){
        assertThrows(NullPointerException.class, () ->
                new User(
                        new Name("Tiago"),
                        new Address("Rua senhor de matosinhos"),
                        null,
                        new Phone( new PhonePrefix("+351"),"918902632")
                )
        );
    }

    @Test
    void constructorWithNullPhoneNumber(){
        assertThrows(NullPointerException.class, () ->
                new User(
                        new Name("Tiago"),
                        new Address("Rua senhor de matosinhos"),
                        new Email("1252008@isep.ipp.pt"),
                        null
                )
        );
    }

}