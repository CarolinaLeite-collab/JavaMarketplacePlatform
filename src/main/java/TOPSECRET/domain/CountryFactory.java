package TOPSECRET.domain;

/**
 * Factory responsible for creating {@link Country} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */
public class CountryFactory {
    public Country createFactory(String countryName) {
            return new Country(countryName);
    }

}
