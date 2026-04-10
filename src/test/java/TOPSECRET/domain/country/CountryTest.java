package TOPSECRET.domain.country;

import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    @Test
    void constructorValidArgumentsCreatesCountry() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertNotNull(country);
        assertEquals("PORTUGAL", country.name().toString());
        assertEquals(new CountryId("PT"), country.identity());
    }

    @Test
    void identityReturnsCountryId() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertTrue(country.identity() instanceof CountryId);
        assertEquals(new CountryId("PT"), country.identity());
    }

    @Test
    void nameReturnsCountryName() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertTrue(country.name() instanceof CountryName);
        assertEquals(new CountryName("Portugal"), country.name());
    }

    @Test
    void getCountryNameReturnsLegacyStringRepresentation() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertEquals("PORTUGAL", country.getCountryName());
    }

    @Test
    void sameAsSameIdReturnsTrue() {
        // SUT
        Country c1 = new Country("Portugal");
        Country c2 = new Country("  portugal  ");

        // Act + Assert
        assertTrue(c1.sameAs(c2));
    }

    @Test
    void sameAsDifferentIdReturnsFalse() {
        // SUT
        Country c1 = new Country("Portugal");
        Country c2 = new Country("Spain");

        // Act + Assert
        assertFalse(c1.sameAs(c2));
    }

    @Test
    void sameAsNullReturnsFalse() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertFalse(country.sameAs(null));
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertFalse(country.sameAs("not a country"));
    }

    @Test
    void sameAsSameInstanceReturnsTrue() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertTrue(country.sameAs(country));
    }

    @Test
    void equalsSameIdReturnsTrue() {
        // SUT
        Country c1 = new Country("Portugal");
        Country c2 = new Country("portugal");

        // Act + Assert
        assertEquals(c1, c2);
    }

    @Test
    void equalsDifferentIdReturnsFalse() {
        // SUT
        Country c1 = new Country("Portugal");
        Country c2 = new Country("Spain");

        // Act + Assert
        assertNotEquals(c1, c2);
    }

    @Test
    void equalsDifferentTypeReturnsFalse() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertNotEquals(country, "not a country");
    }

    @Test
    void equalsNullReturnsFalse() {
        //SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertNotEquals(null, country);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertEquals(country, country);
    }

    @Test
    void hashCodeSameIdReturnsSameHash() {
        // SUT
        Country c1 = new Country("Portugal");
        Country c2 = new Country("portugal");

        // Act + Assert
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void hashCodeReturnsCountryIdHashCode() {
        // SUT
        Country country = new Country("Portugal");

        // Act + Assert
        assertEquals(country.identity().hashCode(), country.hashCode());
    }

}