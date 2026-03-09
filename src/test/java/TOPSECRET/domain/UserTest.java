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
    private Country _countryDouble;
    private Email _emailDouble;
    private Email _emailDouble2;
    private PhonePrefix _phonePerfixDouble;
    private Phone _phoneDouble;
    private User _user;

    @BeforeEach
    void setup() {
        _nameDouble = mock(Name.class);
        when(_nameDouble.get_Name()).thenReturn("Tiago");

        _nameDouble2 = mock(Name.class);
        when(_nameDouble2.get_Name()).thenReturn("Alfredo");

        _countryDouble = mock(Country.class);
        when(_countryDouble.getCountryName()).thenReturn("Portugal");

        _addressDouble = mock(Address.class);
        when(_addressDouble.getStreet()).thenReturn("Rua senhor de matosinhos");
        when(_addressDouble.getDoorNumber()).thenReturn("81");
        when(_addressDouble.getBuildingType()).thenReturn(Address.BuildingType.HOUSE);
        when(_addressDouble.getCity()).thenReturn("Matosinhos");
        when(_addressDouble.getDistrictOrState()).thenReturn("Porto");
        when(_addressDouble.getCountry()).thenReturn(_countryDouble);
        when(_addressDouble.getPostalCode()).thenReturn("4300-111");
        when(_addressDouble.getPostalCodeExtension()).thenReturn(null);

        _emailDouble = mock(Email.class);
        when(_emailDouble.toString()).thenReturn("1252008@isep.ipp.pt");

        _emailDouble2 = mock(Email.class);
        when(_emailDouble2.toString()).thenReturn("test2@email.pt");

        _phonePerfixDouble = mock(PhonePrefix.class);
        when(_phonePerfixDouble.getValue()).thenReturn("+351");

        _phoneDouble = mock(Phone.class);
        when(_phoneDouble.getPrefix()).thenReturn(_phonePerfixDouble);
        when(_phoneDouble.getNationalNumber()).thenReturn("918902632");

        _user = new User(_nameDouble, _addressDouble, _emailDouble, _phoneDouble);
    }

    @Test
    void constructorWithValidArgumentsCreatesUser() {
        // Assert
        assertEquals(_nameDouble, _user.getName());
        assertEquals("1252008@isep.ipp.pt", _user.getEmail());
        assertEquals(_addressDouble, _user.getAddress());
        assertEquals(_phoneDouble, _user.getPhone());
    }

    //Objects.requireNonNull throw NullPointerException (no IllegalArgumentException).
    @Test
    void constructorWithNullName() {

        assertThrows(NullPointerException.class, () -> new User(null, _addressDouble, _emailDouble, _phoneDouble));

    }

    @Test
    void constructorShouldAllowNullAddress() {
        //Act
        User user = new User(_nameDouble, null, _emailDouble, _phoneDouble);
        //Assert
        assertNull(user.getAddress());
    }

    @Test
    void constructorWithNullEmail() {

        assertThrows(NullPointerException.class, () -> new User(_nameDouble, _addressDouble, null, _phoneDouble));
    }

    @Test
    void constructorShouldAllowNullPhoneNumber() {
        //Act
        User user = new User(_nameDouble, _addressDouble, _emailDouble, null);
        //Assert
        assertNull(user.getPhone());
    }

    @Test
    void constructorWithNameAndEmail() {

        User user = new User(_nameDouble, _emailDouble);

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
        assertEquals("Tiago", user.toString());
    }

    @Test
    void testEquals() {

        Name _nameDouble3 = mock(Name.class);
        when(_nameDouble3.get_Name()).thenReturn("Magalhaes");

        User u1 = new User(_nameDouble, _emailDouble);

        User u2 = new User(_nameDouble2, _emailDouble);

        User u3 = new User(_nameDouble3, _emailDouble2);

        String u4 = "user";

        assertEquals(u1, u1);
        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertNotEquals(u1, null);
        assertNotEquals(u1, u4);

    }

    @Test
    void testHashCodeSameEmail() {

        User t = new User(_nameDouble, _emailDouble2);
        User t2 = new User(_nameDouble2, _emailDouble2);

        assertEquals(t.hashCode(), t2.hashCode());

    }

    @Test
    void testHashCodeDifferentEmail() {

        User t = new User(_nameDouble, _emailDouble);
        User t2 = new User(_nameDouble2, _emailDouble2);

        assertNotEquals(t.hashCode(), t2.hashCode());

    }

    @Test
    void constructorShouldAssignDefaultUserRole() {
        // Arrange
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
        // Arrange
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
        // Arrange
        User user = new User(_nameDouble, _emailDouble);
        // Act
        boolean result = user.hasRole(Role.ADMIN);
        // Assert
        assertFalse(result);
    }

    @Test
    void addRoleShouldThrowWhenRoleIsNull() {
        // Arrange
        User user = new User(_nameDouble, _emailDouble);
        // Act & Assert
        assertThrows(NullPointerException.class, () -> user.addRole(null));
    }
}
