package TOPSECRET.domain;

import java.util.List;

public interface ICountryRepo {
    Country registerCountry(String countryName) throws InstantiationException;
    List<Country> getAllCountries();
    Country findByName(String name);
}
