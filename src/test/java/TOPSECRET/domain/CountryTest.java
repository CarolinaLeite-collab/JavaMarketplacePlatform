package TOPSECRET.domain;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.country.CountryFactory;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    @org.junit.jupiter.api.Test
    void identityReturnsCountryId() {
        CountryFactory factory = new CountryFactory();
        Country country = factory.createCountry("PT", "Portugal");
        assertEquals(new TOPSECRET.domain.valueobject.CountryId("PT"), country.identity());
    }

    @org.junit.jupiter.api.Test
    void sameAsConsidersIdEqualityOnly() {
        CountryFactory factory = new CountryFactory();
        Country c1 = factory.createCountry("PT", "Portugal");
        Country c2 = factory.createCountry("PT", "Portugal Alt");

        // Should be same aggregate if ids equal
        assertTrue(c1.sameAs(c2));
    }

    @org.junit.jupiter.api.Test
    void legacyConstructorStillWorks() {
        Country c = new Country("France");
        assertEquals("FRANCE", c.getCountryName());
    }
}