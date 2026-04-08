package TOPSECRET.domain.country;

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

    public Country createCountry(CountryId id, CountryName name) {
        return new Country(id, name);
    }

}
