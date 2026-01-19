package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryRepo {
    private List<Country> _countries;

    public CountryRepo () {
        _countries = new ArrayList<>();
    }

    public Country registerCountry (String countryName, User admin, LocalDate createdDate) {
        Country newCountry = new Country(countryName, admin, createdDate);

        if (findCountry(newCountry))  {
            return null;
        }
        else  {
            _countries.add(newCountry);
        }
        return newCountry;
    }

    private boolean findCountry(Country country) {
        for (Country country1 : _countries) {
            if (country1.equals(country)) {
                return true;
            }
        }
        return false;
    }

    public List<Country> getAllCountries() {
        return Collections.unmodifiableList(_countries);
    }

    // Added helper to lookup a country by name (returns null if not found)
    public Country findByName(String name) {
        if (name == null) return null;
        for (Country c : _countries) {
            if (name.equals(c.getCountryName())) return c;
        }
        return null;
    }

}
