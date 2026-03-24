package TOPSECRET.domain;

import TOPSECRET.ddd.ValueObject;
import TOPSECRET.domain.valueobject.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {
    private ValueObject.Name _nameDouble;
    private Address _addressDouble;
    private Email _emailDouble;
    private Phone _phoneDouble;

    @BeforeEach
    void setUp() {
        _nameDouble = mock(ValueObject.Name.class);
        _addressDouble = mock(Address.class);
        _emailDouble = mock(Email.class);
        _phoneDouble = mock(Phone.class);
    }

    @Test
    void constructorWithValidArgumentsCreatesUser() {

        // Arrange
        when(_emailDouble.toString()).thenReturn("1252008@isep.ipp.pt");

        // Act & SUT
        User user = new User(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);

        // Assert
        assertEquals(_nameDouble, user.getName());
        assertEquals("1252008@isep.ipp.pt", user.getEmail());
        assertEquals(_addressDouble, user.getAddress());
        assertEquals(_phoneDouble, user.getPhone());
    }

    @Test
    void constructorShouldThrowExceptionWhenNameIsNull() {

        // Act & Assert & SUT
        assertThrows(NullPointerException.class, () -> new User(null, _addressDouble, _emailDouble, _phoneDouble));
    }

    @Test
    void constructorShouldAllowNullAddress() {

       // Act & Assert & SUT
        assertDoesNotThrow (() -> new User(_nameDouble, null, _emailDouble, _phoneDouble));
    }

    @Test
    void constructorShouldThrowExceptionWhenEmailIsNull() {

        // Act & Assert & SUT
        assertThrows(NullPointerException.class, () -> new User(_nameDouble, _addressDouble, null, _phoneDouble));
    }

    @Test
    void constructorShouldAllowNullPhoneNumber() {

        // Act & Assert & SUT
        assertDoesNotThrow (() -> new User(_nameDouble, _addressDouble, _emailDouble, null));
    }

    @Test
    void constructorShouldBuildUserWithNameAndEmail() {

        // Arrange
        when(_emailDouble.toString()).thenReturn("1252008@isep.ipp.pt");

        // Act & SUT
        User user = new User(_nameDouble, null, _emailDouble, null);

        // Assert
        assertEquals(_nameDouble, user.getName());
        assertEquals("1252008@isep.ipp.pt", user.getEmail());
        assertNull(user.getAddress());
        assertNull(user.getPhone());
    }

    @Test
    void toStringShouldReturnName() {

        // Arrange
        when(_nameDouble.toString()).thenReturn("Tiago");
        User user = new User(_nameDouble, _emailDouble);

        // Act & SUT
        String userName = user.toString();

        // Assert
        assertEquals("Tiago", userName);
    }

    @Test
    void testEquals() {

        // Arrange
        ValueObject.Name nameDouble = mock(ValueObject.Name.class);
        ValueObject.Name nameDouble2 = mock(ValueObject.Name.class);
        ValueObject.Name nameDouble3 = mock(ValueObject.Name.class);
        Email emailDouble2 = mock(Email.class);

        String u4 = "user";

        // SUT
        User u1 = new User(nameDouble, _emailDouble);
        User u2 = new User(nameDouble2, _emailDouble);
        User u3 = new User(nameDouble3, emailDouble2);

        // Assert & Act
        assertEquals(u1, u1);
        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertNotEquals(u1, null);
        assertNotEquals(u1, u4);

    }

    @Test
    void hashCodeShouldBeEqualForSameEmail() {

        // Arrange
        ValueObject.Name nameDouble2 = mock(ValueObject.Name.class);
        Email emailDouble2 = mock(Email.class);
        User user1 = new User(_nameDouble, emailDouble2);
        User user2 = new User(nameDouble2, emailDouble2);

        // Act & SUT
        int hash1 = user1.hashCode();
        int hash2 = user2.hashCode();

        // Assert
        assertEquals(hash1, hash2);

    }

    @Test
    void hashCodeShouldBeDifferentForDifferentEmail() {

        // Arrange
        ValueObject.Name nameDouble2 = mock(ValueObject.Name.class);
        Email emailDouble2 = mock(Email.class);

        // SUT
        User user1 = new User(_nameDouble, _emailDouble);
        User user2 = new User(nameDouble2, emailDouble2);

        // Act
        int hash1 = user1.hashCode();
        int hash2 = user2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);

    }

    @Test
    void constructorShouldAssignDefaultUserRole() {

        // Arrange & SUT
        User user = new User(_nameDouble, _emailDouble);

        // Act
        boolean hasUserRole = user.hasRole(Role.USER);

        // Assert
        assertTrue(hasUserRole);
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(Role.USER));
    }

    @Test
    void addRoleShouldAddAdminRoleWithoutRemovingDefaultUserRole() {

        // Arrange & SUT
        User user = new User(_nameDouble, _emailDouble);

        // Act
        user.addRole(Role.ADMIN);

        // Assert
        assertTrue(user.hasRole(Role.USER));
        assertTrue(user.hasRole(Role.ADMIN));
        assertEquals(2, user.getRoles().size());
    }

    @Test
    void hasRoleShouldReturnFalseWhenUserDoesNotHaveAdminRole() {

        // Arrange & SUT
        User user = new User(_nameDouble, _emailDouble);

        // Act
        boolean result = user.hasRole(Role.ADMIN);

        // Assert
        assertFalse(result);
    }

    @Test
    void addRoleShouldThrowWhenRoleIsNull() {

        // Arrange & SUT
        User user = new User(_nameDouble, _emailDouble);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> user.addRole(null));
    }
}
