package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructorWithValidArgumentsCreatesUser(){
        User user = new User(
                new Name("Tiago"),
                new Address ("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", Address.Country.PORTUGAL, "4300-111", null ),
                new Email ("1252008@isep.ipp.pt"),
                new Phone( new PhonePrefix("+351"),"918902632")
        );

        assertEquals("Tiago", user.getName().toString());
        assertEquals("1252008@isep.ipp.pt", user.getEmail().toString());
        assertEquals("PORTUGAL, Matosinhos (Porto), Rua senhor de matosinhos, 81, HOUSE, 4300-111", user.getAddress().toString());
        assertEquals("+351918902632", user.getPhone().toString());
    }
//Objects.requireNonNull lança NullPointerException (não IllegalArgumentException).
    @Test
    void constructorWithNullName(){
        assertThrows(NullPointerException.class, () ->
                new User(
                        null,
                    new Address ("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", Address.Country.PORTUGAL, "4300-111", null ),
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
                        new Address ("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", Address.Country.PORTUGAL, "4300-111", null ),
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
                        new Address ("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", Address.Country.PORTUGAL, "4300-111", null ),
                        new Email("1252008@isep.ipp.pt"),
                        null
                )
        );
    }

}