package TOPSECRET.domain.country;

import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    @Test
    void constructorValidArgumentsCreatesCountry() {
        Country country = new Country("Portugal");

        assertNotNull(country);
        assertEquals("PORTUGAL", country.name().toString());
        assertEquals(new CountryId("PT"), country.identity());
    }

    @Test
    void identityReturnsCountryId() {
        Country country = new Country("Portugal");

        assertTrue(country.identity() instanceof CountryId);
        assertEquals(new CountryId("PT"), country.identity());
    }

    @Test
    void nameReturnsCountryName() {
        Country country = new Country("Portugal");

        assertTrue(country.name() instanceof CountryName);
        assertEquals(new CountryName("Portugal"), country.name());
    }

    @Test
    void getCountryNameReturnsLegacyStringRepresentation() {
        Country country = new Country("Portugal");

        assertEquals("PORTUGAL", country.getCountryName());
    }

    @Test
    void sameAsSameIdReturnsTrue() {
        Country c1 = new Country("Portugal");
        Country c2 = new Country("  portugal  ");

        assertTrue(c1.sameAs(c2));
    }

    @Test
    void sameAsDifferentIdReturnsFalse() {
        Country c1 = new Country("Portugal");
        Country c2 = new Country("Spain");

        assertFalse(c1.sameAs(c2));
    }

    @Test
    void sameAsNullReturnsFalse() {
        Country country = new Country("Portugal");

        assertFalse(country.sameAs(null));
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        Country country = new Country("Portugal");

        assertFalse(country.sameAs("not a country"));
    }

    @Test
    void sameAsSameInstanceReturnsTrue() {
        Country country = new Country("Portugal");

        assertTrue(country.sameAs(country));
    }

    @Test
    void equalsSameIdReturnsTrue() {
        Country c1 = new Country("Portugal");
        Country c2 = new Country("portugal");

        assertEquals(c1, c2);
    }

    @Test
    void equalsDifferentIdReturnsFalse() {
        Country c1 = new Country("Portugal");
        Country c2 = new Country("Spain");

        assertNotEquals(c1, c2);
    }

    @Test
    void equalsDifferentTypeReturnsFalse() {
        Country country = new Country("Portugal");

        assertNotEquals(country, "not a country");
    }

    @Test
    void equalsNullReturnsFalse() {
        Country country = new Country("Portugal");

        assertNotEquals(null, country);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        Country country = new Country("Portugal");

        assertEquals(country, country);
    }

    @Test
    void hashCodeSameIdReturnsSameHash() {
        Country c1 = new Country("Portugal");
        Country c2 = new Country("portugal");

        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void hashCodeReturnsCountryIdHashCode() {
        Country country = new Country("Portugal");

        assertEquals(country.identity().hashCode(), country.hashCode());
    }

}