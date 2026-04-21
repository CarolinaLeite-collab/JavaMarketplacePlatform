package MITELOVERS.persistence.mem;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;

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
        return DATA.values();
    }

    @Override
    public Optional<Country> ofIdentity(CountryId id) {
        if (!containsOfIdentity(id))
        {
            return Optional.empty();
        } else {
            return Optional.of(DATA.get(id));
        }
    }

    @Override
    public boolean containsOfIdentity(CountryId id) {
        return DATA.containsKey(id);
    }

    @Override
    public Country addCountry(String countryName) {
        Country country = _countryFactory.createCountry(countryName);
        if (containsOfIdentity(country.identity()))
            throw new IllegalArgumentException("Country already exists in the repository");

        return save(country);
    }

    @Override
    public List<CountryId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }
}
