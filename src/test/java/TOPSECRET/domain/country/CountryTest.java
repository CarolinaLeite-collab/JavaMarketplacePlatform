package TOPSECRET.domain.country;

import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CountryTest {

    private CountryId _countryIdDouble;
    private CountryName _countryNameDouble;

    @BeforeEach
    void setUp() {
        _countryIdDouble = mock(CountryId.class);
        _countryNameDouble = mock(CountryName.class);
    }

    @Test
    void constructorValidArgumentsCreatesCountry() {
        // SUT
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertNotNull(country);
    }

    @Test
    void identityReturnsCountryId() {
        // SUT
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertSame(_countryIdDouble, country.identity());
    }

    @Test
    void nameReturnsCountryName() {
        // SUT
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertSame(_countryNameDouble, country.name());
    }

    @Test
    void getCountryNameReturnsNameString() {
        // Arrange
        CountryName _realNameDouble = new CountryName("Portugal");

        // SUT
        Country country = new Country(_countryIdDouble, _realNameDouble);

        // Assert
        assertEquals("PORTUGAL", country.getCountryName());
    }

    @Test
    void isNamedWithSameCountryNameReturnsTrue() {
        // Arrange
        CountryName _realNameDouble = new CountryName("Portugal");

        // SUT
        Country country = new Country(_countryIdDouble, _realNameDouble);

        boolean result = country.isNamed(new CountryName("Portugal"));

        // Assert
        assertTrue(result);
    }

    @Test
    void isNamedWithDifferentCountryNameReturnsFalse() {
        // Arrange
        CountryName _realNameDouble = new CountryName("Portugal");

        // SUT
        Country country = new Country(_countryIdDouble, _realNameDouble);

        boolean result = country.isNamed(new CountryName("Deutschland"));

        // Assert
        assertFalse(result);
    }

    @Test
    void isNamedWithSameStringReturnsTrue() {
        // Arrange
        CountryName _realNameDouble = new CountryName("Portugal");

        // SUT
        Country country = new Country(_countryIdDouble, _realNameDouble);


        boolean result = country.isNamed("Portugal");

        // Assert
        assertTrue(result);
    }

    @Test
    void isNamedWithNullStringReturnsFalse() {
        // SUT
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertFalse(country.isNamed((String) null));
    }

    @Test
    void isNamedWithDifferentStringReturnsFalse() {
        // Arrange
        CountryName _realNameDouble = new CountryName("Portugal");

        // SUT
        Country country = new Country(_countryIdDouble, _realNameDouble);

        boolean result = country.isNamed("Deutschland");

        // Assert
        assertFalse(result);
    }

    @Test
    void isOneOfContainsNameReturnsTrue() {
        // Arrange
        CountryName _realNameDouble = new CountryName("Portugal");

        // SUT
        Country country = new Country(_countryIdDouble, _realNameDouble);

        boolean result = country.isOneOf(new CountryName("Deutschland"), new CountryName("Portugal"));

        // Assert
        assertTrue(result);
    }

    @Test
    void isOneOfNotContainsNameReturnsFalse() {
        // Arrange
        CountryName _realNameDouble = new CountryName("Portugal");

        // SUT
        Country country = new Country(_countryIdDouble, _realNameDouble);

        // Act
        boolean result = country.isOneOf(new CountryName("Deutschland"), new CountryName("France"));

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameIdReturnsSameHashWithRealIds() {
        // Arrange
        CountryId realId = new CountryId("PT");

        Country c1 = new Country(realId, _countryNameDouble);
        Country c2 = new Country(realId, mock(CountryName.class));

        // Assert
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void sameAsSameIdReturnsTrue() {
        // Arrange
        CountryId _sharedIdDouble = mock(CountryId.class);
        Country c1 = new Country(_sharedIdDouble, _countryNameDouble);
        Country c2 = new Country(_sharedIdDouble, mock(CountryName.class));

        // SUT
        boolean result = c1.sameAs(c2);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsDifferentIdReturnsFalse() {
        // Arrange
        Country c1 = new Country(_countryIdDouble, _countryNameDouble);
        Country c2 = new Country(mock(CountryId.class), _countryNameDouble);

        // SUT
        boolean result = c1.sameAs(c2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsNullReturnsFalse() {
        // SUT
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertFalse(country.sameAs(null));
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        // SUT
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertFalse(country.sameAs("not a country"));
    }

    @Test
    void sameAsSameInstanceReturnsTrue() {
        // SUT
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertTrue(country.sameAs(country));
    }

    @Test
    void equalsSameIdReturnsTrue() {
        // Arrange
        CountryId _sharedIdDouble = mock(CountryId.class);
        Country c1 = new Country(_sharedIdDouble, _countryNameDouble);
        Country c2 = new Country(_sharedIdDouble, mock(CountryName.class));

        // SUT + Assert
        assertEquals(c1, c2);
    }

    @Test
    void equalsDifferentIdReturnsFalse() {
        // Arrange
        Country c1 = new Country(_countryIdDouble, _countryNameDouble);
        Country c2 = new Country(mock(CountryId.class), _countryNameDouble);

        // SUT + Assert
        assertNotEquals(c1, c2);
    }

    @Test
    void equalsDifferentTypeReturnsFalse() {
        // Arrange
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertNotEquals(country, "not a country");
    }

    @Test
    void equalsNullReturnsFalse() {
        // SUT
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertNotEquals(null, country);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // SUT
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertTrue(country.equals(identity(country)));
    }

    @Test
    void hashCodeSameIdReturnsSameHash() {
        // Arrange
        CountryId _sharedIdDouble = mock(CountryId.class);
        Country c1 = new Country(_sharedIdDouble, _countryNameDouble);
        Country c2 = new Country(_sharedIdDouble, mock(CountryName.class));

        // Assert
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void hashCodeReturnsCountryIdHashCode() {
        // Arrange
        Country country = new Country(_countryIdDouble, _countryNameDouble);

        // Assert
        assertEquals(_countryIdDouble.hashCode(), country.hashCode());
    }

    private Country identity(Country country) {
        return country;
    }

}