package TOPSECRET.domain;

/**
 * Factory responsible for creating {@link Country} instances.
 * <p>
 * @throws IllegalArgumentException if genreName is invalid (as defined by {@link Country}'s constructor).
 * </p>
 */

public class CountryFactory {

    public Country createCountry(String countryName) {
            return new Country(countryName);
    }

}
