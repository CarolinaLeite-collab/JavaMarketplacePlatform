package MITELOVERS.persistence.mem;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;

import java.util.*;

/**
 * In-memory repository for Country aggregates.
 * <p>
 * This repository is strictly responsible for persistence concerns.
 * </p>
 */
public class MemCountryRepo implements ICountryRepo {
    private final Map<CountryId, Country> DATA = new HashMap<>();

    public MemCountryRepo() {
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
        if (!containsOfIdentity(id)) {
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
    public List<CountryId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }
}
