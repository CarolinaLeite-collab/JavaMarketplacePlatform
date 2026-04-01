package TOPSECRET.domain;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.valueobject.CountryId;

import java.util.Optional;

public interface ICountryRepo extends IRepository<CountryId, Country> {

    // IRepository provides: save, findAll, ofIdentity, containsOfIdentity

    // Domain-specific helper
    Optional<Country> findByName(String name);

    // Legacy compatibility methods
    default Country registerCountry(String countryName) {
        Country country = new Country(countryName);
        if (containsOfIdentity(country.identity())) return null;
        return save(country);
    }

    default java.util.List<Country> getAllCountries() {
        java.util.List<Country> list = new java.util.ArrayList<>();
        for (Country c : findAll()) list.add(c);
        return java.util.List.copyOf(list);
    }
}
