package TOPSECRET.domain;

import java.util.List;

public interface ICountryRepo {

    Country registerCountry(String countryName);

    List<Country> getAllCountries();

    Country findByName(String name);
}
