package TOPSECRET.domain;

import java.util.Locale;

/**
 * Factory responsible for creating {@link Country} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */
public class CountryFactory {
    public Country createClass(String countryName) {
            return new Country(countryName);
    }

}
