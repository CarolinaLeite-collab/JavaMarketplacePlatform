package TOPSECRET.domain;

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
