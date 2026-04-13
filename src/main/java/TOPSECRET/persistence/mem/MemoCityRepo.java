package TOPSECRET.persistence.mem;

import TOPSECRET.domain.city.City;
import TOPSECRET.domain.repository.ICityRepo;
import TOPSECRET.domain.valueobject.CityId;

import java.util.*;

public class MemoCityRepo implements ICityRepo {

    private final Map<CityId, City> DATA = new HashMap<>();

    @Override
    public City save(City city) {
        DATA.put(city.identity(), city);
        return city;
    }

    @Override
    public City addCity(City city) {
        if (containsOfIdentity(city.identity())) {
            throw new IllegalStateException("City already exists for this country");
        }
        return save(city);
    }

    @Override
    public Iterable<City> findAll() {
        return DATA.values();
    }

    public List<CityId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }


    @Override
    public Optional<City> ofIdentity(CityId cityId) {
        if (!containsOfIdentity(cityId)) {
            return Optional.empty();
        } else  {
            return Optional.of(DATA.get(cityId));
        }
    }

    @Override
    public boolean containsOfIdentity(CityId cityId) {
        return DATA.containsKey(cityId);
    }
}