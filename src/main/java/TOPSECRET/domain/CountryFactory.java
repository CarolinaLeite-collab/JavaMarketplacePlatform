package TOPSECRET.domain;

public class CountryFactory {
    public Country newCountry(String countryName)  throws IllegalArgumentException {
        return new Country(countryName);
    }
}
