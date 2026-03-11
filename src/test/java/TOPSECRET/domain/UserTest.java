package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {
    private Name _nameDouble;
    private Name _nameDouble2;
    private Address _addressDouble;
    private Email _emailDouble;
    private Email _emailDouble2;
    private Phone _phoneDouble;

    @BeforeEach
    void setup() {
        _nameDouble = mock(Name.class);

        _nameDouble2 = mock(Name.class);

        _addressDouble = mock(Address.class);

        _emailDouble = mock(Email.class);

        _emailDouble2 = mock(Email.class);

        _phoneDouble = mock(Phone.class);

    }

    @Test
    void constructorWithValidArgumentsCreatesUser() {

        // arrange
        when(_emailDouble.toString()).thenReturn("1252008@isep.ipp.pt");

        // act & SUT
        User user = new User(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        // assert
        assertEquals(_nameDouble, user.getName());
        assertEquals("1252008@isep.ipp.pt", user.getEmail());
        assertEquals(_addressDouble, user.getAddress());
        assertEquals(_phoneDouble, user.getPhone());
    }

    //Objects.requireNonNull throw NullPointerException (no IllegalArgumentException).
    @Test
    void constructorWithNullName() {

        // act & assert & SUT
        assertThrows(NullPointerException.class, () -> new User(null, _addressDouble, _emailDouble, _phoneDouble));

    }

    @Test
    void constructorShouldAllowNullAddress() {

       // act & assert & SUT
        assertDoesNotThrow (() -> new User(_nameDouble, null, _emailDouble, _phoneDouble));
    }

    @Test
    void constructorWithNullEmail() {

        // act & assert & SUT
        assertThrows(NullPointerException.class, () -> new User(_nameDouble, _addressDouble, null, _phoneDouble));
    }

    @Test
    void constructorShouldAllowNullPhoneNumber() {

        // act & assert & SUT
        assertDoesNotThrow (() -> new User(_nameDouble, _addressDouble, _emailDouble, null));
    }

    @Test
    void constructorWithNameAndEmail() {

        // arrange
        when(_emailDouble.toString()).thenReturn("1252008@isep.ipp.pt");

        // act & SUT
        User user = new User(_nameDouble, null, _emailDouble, null);

        // assert
        assertEquals(_nameDouble, user.getName());
        assertEquals("1252008@isep.ipp.pt", user.getEmail());
        assertNull(user.getAddress());
        assertNull(user.getPhone());
    }

    @Test
    void testToString() {

        // arrange
        when(_nameDouble.toString()).thenReturn("Tiago");
        User user = new User(_nameDouble, _emailDouble);

        // act & SUT
        String userName = user.toString();

        // assert
        assertEquals("Tiago", userName);
    }

    @Test
    void testEquals() {

        // arrange
        Name _nameDouble3 = mock(Name.class);
        when(_nameDouble3.get_Name()).thenReturn("Magalhaes");

        String u4 = "user";

        User u1 = new User(_nameDouble, _emailDouble);

        User u2 = new User(_nameDouble2, _emailDouble);

        User u3 = new User(_nameDouble3, _emailDouble2);

        // assert & act & SUT
        assertEquals(u1, u1);
        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertNotEquals(u1, null);
        assertNotEquals(u1, u4);

    }

    @Test
    void testHashCodeSameEmail() {

        // arrange
        User t = new User(_nameDouble, _emailDouble2);
        User t2 = new User(_nameDouble2, _emailDouble2);

        // act & SUT
        int u = t.hashCode();
        int u2 = t2.hashCode();

        // assert
        assertEquals(u, u2);

    }

    @Test
    void testHashCodeDifferentEmail() {

        // arrange
        User t = new User(_nameDouble, _emailDouble);
        User t2 = new User(_nameDouble2, _emailDouble2);

        // act & SUT
        int u = t.hashCode();
        int u2 = t2.hashCode();

        // assert
        assertNotEquals(u, u2);

    }

    @Test
    void constructorShouldAssignDefaultUserRole() {

        // arrange
        User user = new User(_nameDouble, _emailDouble);

        // act & SUT
        boolean hasUserRole = user.hasRole(Role.USER);

        // assert
        assertTrue(hasUserRole);
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(Role.USER));
    }

    @Test
    void addRoleShouldAddAdminRoleWithoutRemovingDefaultUserRole() {

        // arrange
        User user = new User(_nameDouble, _emailDouble);

        // act & SUT
        user.addRole(Role.ADMIN);

        // assert
        assertTrue(user.hasRole(Role.USER));
        assertTrue(user.hasRole(Role.ADMIN));
        assertEquals(2, user.getRoles().size());
    }

    @Test
    void hasRoleShouldReturnFalseWhenUserDoesNotHaveAdminRole() {

        // assert
        User user = new User(_nameDouble, _emailDouble);

        // act & SUT
        boolean result = user.hasRole(Role.ADMIN);

        // assert
        assertFalse(result);
    }

    @Test
    void addRoleShouldThrowWhenRoleIsNull() {

        // arrange
        User user = new User(_nameDouble, _emailDouble);

        // act & assert & SUT
        assertThrows(NullPointerException.class, () -> user.addRole(null));
    }
}
