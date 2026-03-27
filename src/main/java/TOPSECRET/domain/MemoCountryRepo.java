package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing countries in the system.
 * <p>
 * Provides functionality to register new countries, prevent duplicates,
 * and retrieve existing countries. The repository maintains a list of unique countries
 * based on their name.
 */

public class MemoCountryRepo implements ICountryRepo {
    private final List<Country> _countries;
    private final CountryFactory _countryFactory;

    public MemoCountryRepo(CountryFactory countryFactory){
        _countries = new ArrayList<>();
        _countryFactory =  countryFactory;
    }

    @Override
    public Country registerCountry(String countryName) {
        Country newCountry = _countryFactory.createCountry(countryName);

        if (existsCountry(newCountry))  {
            return null;
        }
        else  {
            _countries.add(newCountry);
            return newCountry;
        }
    }

    @Override
    public List<Country> getAllCountries() {
        return List.copyOf(_countries);
    }

    @Override
    public Country findByName(String name) {
        if (name == null) {
            return null;
        }
        String normalized = name.trim().replaceAll("\\s+", " ").toUpperCase();
        for (Country c : _countries) {
            if (c.isNamed(normalized))
                return c;
        }
        return null;
    }

    private boolean existsCountry(Country country) {
        for (Country country1 : _countries) {
            if (country1.equals(country)) {
                return true;
            }
        }
        return false;
    }
}
