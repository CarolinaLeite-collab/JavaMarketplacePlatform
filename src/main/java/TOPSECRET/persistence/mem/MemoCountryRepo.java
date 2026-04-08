package TOPSECRET.persistence.mem;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.country.CountryFactory;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;

import java.util.*;

/**
 * In-memory repository for Country aggregates.
 */
public class MemoCountryRepo implements ICountryRepo {
    private final Map<CountryId, Country> DATA = new HashMap<CountryId, Country>();
    private final CountryFactory _countryFactory;

    public MemoCountryRepo(CountryFactory countryFactory) {
        _countryFactory = countryFactory;
    }

    @Override
    public Country save(Country entity) {
        DATA.put(entity.identity(), entity);
        return entity;
    }

    @Override
    public Iterable<Country> findAll() {
        return List.copyOf(DATA.values());
    }

    @Override
    public Optional<Country> ofIdentity(CountryId id) {
        return Optional.ofNullable(DATA.get(id));
    }

    @Override
    public boolean containsOfIdentity(CountryId id) {
        return DATA.containsKey(id);
    }

    @Override
    public Optional<Country> findByName(String name) {
        if (name == null) return Optional.empty();
        CountryName target = new CountryName(name);
        return DATA.values().stream()
                .filter(c -> c.isNamed(target))
                .findFirst();
    }

    public Country addCountry(String isoCode, String countryName) {
        CountryId id = new CountryId(isoCode);
        if (containsOfIdentity(id))
            throw new IllegalArgumentException("Country already exists in the repository");
        Country country = _countryFactory.createCountry(isoCode, countryName);
        return save(country);
    }
}
