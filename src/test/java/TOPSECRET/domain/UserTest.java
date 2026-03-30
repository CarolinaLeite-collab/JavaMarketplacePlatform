package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {
    private Name _nameDouble;
    private Address _addressDouble;
    private Phone _phoneDouble;

    @BeforeEach
    void setUp() {
        _nameDouble = mock(Name.class);
        _addressDouble = mock(Address.class);
        _phoneDouble = mock(Phone.class);
    }

    @Test
    void constructorWithValidArgumentsCreatesUser() {

        // Arrange
        Email emailDouble = mock(Email.class);
        when(emailDouble.toString()).thenReturn("1252008@isep.ipp.pt");

        // Act & SUT
        User user = new User(_nameDouble, _addressDouble, emailDouble, _phoneDouble);

        // Assert
        assertEquals(_nameDouble, user.getName());
        assertEquals("1252008@isep.ipp.pt", user.getEmail());
        assertEquals(_addressDouble, user.getAddress());
        assertEquals(_phoneDouble, user.getPhone());
    }

    @Test
    void constructorShouldAllowNullAddress() {

        // Arrange
        Email emailDouble = mock(Email.class);

       // Act & Assert & SUT
        assertDoesNotThrow (() -> new User(_nameDouble, null, emailDouble, _phoneDouble));
    }

    @Test
    void constructorShouldThrowExceptionWhenEmailIsNull() {

        // Act & Assert & SUT
        assertThrows(NullPointerException.class, () -> new User(_nameDouble, _addressDouble, null, _phoneDouble));
    }

    @Test
    void constructorShouldAllowNullPhoneNumber() {

        // Arrange
        Email emailDouble = mock(Email.class);

        // Act & Assert & SUT
        assertDoesNotThrow (() -> new User(_nameDouble, _addressDouble, emailDouble, null));
    }

    @Test
    void constructorShouldBuildUserWithNameAndEmail() {

        // Arrange
        Email emailDouble = mock(Email.class);
        when(emailDouble.toString()).thenReturn("1252008@isep.ipp.pt");

        // Act & SUT
        User user = new User(_nameDouble, null, emailDouble, null);

        // Assert
        assertEquals(_nameDouble, user.getName());
        assertEquals("1252008@isep.ipp.pt", user.getEmail());
        assertNull(user.getAddress());
        assertNull(user.getPhone());
    }

    @Test
    void toStringShouldReturnName() {

        // Arrange
        Email emailDouble = mock(Email.class);
        when(_nameDouble.toString()).thenReturn("Tiago");
        User user = new User(_nameDouble, emailDouble); // SUT

        // Act
        String userName = user.toString();

        // Assert
        assertEquals("Tiago", userName);
    }

    @Test
    void testEquals() {

        // Arrange
        Name nameDouble = mock(Name.class);
        Name nameDouble2 = mock(Name.class);
        Name nameDouble3 = mock(Name.class);
        Email emailDouble = mock(Email.class);
        Email emailDouble2 = mock(Email.class);

        String u4 = "user";

        // SUT
        User u1 = new User(nameDouble, emailDouble);
        User u2 = new User(nameDouble2, emailDouble);
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
        Name nameDouble2 = mock(Name.class);
        Email emailDouble = mock(Email.class);
        User user1 = new User(_nameDouble, emailDouble);
        User user2 = new User(nameDouble2, emailDouble);

        // Act & SUT
        int hash1 = user1.hashCode();
        int hash2 = user2.hashCode();

        // Assert
        assertEquals(hash1, hash2);

    }

    @Test
    void hashCodeShouldBeDifferentForDifferentEmail() {

        // Arrange
        Name nameDouble2 = mock(Name.class);
        Email emailDouble = mock(Email.class);
        Email emailDouble2 = mock(Email.class);

        // SUT
        User user1 = new User(_nameDouble, emailDouble);
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
        Email emailDouble = mock(Email.class);
        User user = new User(_nameDouble, emailDouble);

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
        Email emailDouble = mock(Email.class);
        User user = new User(_nameDouble, emailDouble);

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
        Email emailDouble = mock(Email.class);
        User user = new User(_nameDouble, emailDouble);

        // Act
        boolean result = user.hasRole(Role.ADMIN);

        // Assert
        assertFalse(result);
    }

    @Test
    void addRoleShouldThrowWhenRoleIsNull() {

        // Arrange & SUT
        Email emailDouble = mock(Email.class);
        User user = new User(_nameDouble, emailDouble);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> user.addRole(null));
    }

    @Test
    void hasEmailShouldReturnTrueWhenEmailMatches() {

        // Arrange
        // Arrange
        Email emailDouble = mock(Email.class);
        User user = new User(_nameDouble, emailDouble); // SUT

        // Act
        boolean result = user.hasEmail(emailDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void hasEmailShouldReturnFalseWhenEmailDoesNotMatch() {

        // Arrange
        Email emailDouble = mock(Email.class);
        Email otherEmailDouble = mock(Email.class);
        User user = new User(_nameDouble, emailDouble); // SUT

        // Act
        boolean result = user.hasEmail(otherEmailDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void identityShouldReturnUserIDBasedOnEmail() {

        // Arrange
        Email emailDouble = mock(Email.class);
        User user = new User(_nameDouble, emailDouble); // SUT

        // Act
        UserID identity = user.identity();

        // Assert
        assertNotNull(identity);
    }

    @Test
    void sameAsShouldReturnTrueWhenSameEmail() {

        // Arrange
        Name otherNameDouble = mock(Name.class);
        Email emailDouble = mock(Email.class);
        User user1 = new User(_nameDouble, emailDouble); // SUT
        User user2 = new User(otherNameDouble, emailDouble);

        // Act
        boolean result = user1.sameAs(user2);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenDifferentEmail() {

        // Arrange
        Name otherNameDouble = mock(Name.class);
        Email emailDouble = mock(Email.class);
        Email otherEmailDouble = mock(Email.class);
        User user1 = new User(_nameDouble, emailDouble); // SUT
        User user2 = new User(otherNameDouble, otherEmailDouble);

        // Act
        boolean result = user1.sameAs(user2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenObjectIsNotUser() {

        // Arrange
        Email emailDouble = mock(Email.class);
        User user = new User(_nameDouble, emailDouble); // SUT
        String notAUser = "not a user";

        // Act
        boolean result = user.sameAs(notAUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(NullPointerException.class, () -> new UserFactory().createUser(null, new Email("a@b.com")));
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        assertThrows(NullPointerException.class, () -> new UserFactory().createUser(new Name("Tiago"), null));
    }
}
