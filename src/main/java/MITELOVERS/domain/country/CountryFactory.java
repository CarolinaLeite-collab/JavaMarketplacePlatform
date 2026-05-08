package MITELOVERS.domain.country;


import MITELOVERS.domain.valueobject.CountryId;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link Country} instances.
 */

@Component
public class CountryFactory {

    public Country createCountry(String countryName) {
        return new Country(countryName);
    }

    public Country createCountry(CountryId countryId, String countryName) {
        return new Country(countryId, countryName);
    }


}
