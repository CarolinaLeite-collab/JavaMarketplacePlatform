package TOPSECRET.domain;

import TOPSECRET.domain.city.City;
import TOPSECRET.domain.repository.ICityRepo;
import TOPSECRET.domain.valueobject.CityId;

import java.util.*;

public class MemoCityRepo implements ICityRepo {

    private final Map<CityId, City> _cities = new HashMap<>();

    @Override
    public City save(City city) {
        if (containsOfIdentity(city.identity())) {
            throw new IllegalStateException("City already exists for this country");
        }
        _cities.put(city.identity(), city);
        return city;
    }

    @Override
    public Iterable<City> findAll() {
        return List.copyOf(_cities.values());
    }

    @Override
    public Optional<City> ofIdentity(CityId cityId) {
        return Optional.ofNullable(_cities.get(cityId));
    }

    @Override
    public boolean containsOfIdentity(CityId cityId) {
        return _cities.containsKey(cityId);
    }
}