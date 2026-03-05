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
public class CountryRepo {
    private final List<Country> _countries;
    private final CountryFactory _countryFactory;

    public CountryRepo () {
        _countryFactory = new CountryFactory();
        _countries = new ArrayList<>();
    }

    public Country registerCountry (String countryName) throws InstantiationException {

        Country newCountry = _countryFactory.create(countryName);

        if (existsCountry(newCountry))  {
            return null;
        }
        else  {
            _countries.add(newCountry);
        }
        return newCountry;
    }

    private boolean existsCountry(Country country) {
        for (Country country1 : _countries) {
            if (country1.equals(country)) {
                return true;
            }
        }
        return false;
    }

    /**
    * Returns a list of all countries for unit tests.
    */
    public List<Country> getAllCountries() {
        return List.copyOf(_countries);
    }

    // Added helper to lookup a country by name (returns null if not found)
    public Country findByName(String name) {
        if (name == null) return null;
        String normalized = name.trim();
        for (Country c : _countries) {
            if (normalized.equalsIgnoreCase(c.getCountryName())) return c;
        }
        return null;
    }


}
