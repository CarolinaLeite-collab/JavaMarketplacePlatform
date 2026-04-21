package MITELOVERS.domain.country;


/**
 * Factory responsible for creating {@link Country} instances.
 */
public class CountryFactory {

    public Country createCountry(String countryName) {
        return new Country(countryName);
    }


}
