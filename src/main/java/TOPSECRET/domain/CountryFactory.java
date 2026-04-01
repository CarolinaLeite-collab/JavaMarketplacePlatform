package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;

/**
 * Factory responsible for creating {@link Country} instances.
 */
public class CountryFactory {

    public Country createCountry(String isoCode, String countryName) {
        CountryId id = new CountryId(isoCode);
        CountryName name = new CountryName(countryName);
        return new Country(id, name);
    }

    // Backwards compatible: create from single countryName (legacy tests)
    public Country createCountry(String countryName) {
        // Use normalized country name as pseudo-id to preserve legacy behavior
        CountryName name = new CountryName(countryName);
        CountryId id = new CountryId(name.value().substring(0, Math.min(2, name.value().length())));
        return new Country(id, name);
    }

    public Country createCountry(CountryId id, CountryName name) {
        return new Country(id, name);
    }

}
