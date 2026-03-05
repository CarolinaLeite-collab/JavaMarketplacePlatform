package TOPSECRET.domain;

import java.util.Locale;

/**
 * Factory responsible for creating {@link Country} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */
public class CountryFactory {
    public Country create(String countryName)  throws InstantiationException {
        try {
            return new Country(countryName);
        }
        catch (final Exception e) {
            throw new InstantiationException("Unable to instantiate Country: " + e.getMessage());
        }
    }

}
