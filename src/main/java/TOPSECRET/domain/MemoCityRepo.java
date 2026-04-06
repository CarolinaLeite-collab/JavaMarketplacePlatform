package TOPSECRET.domain;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.repository.ICityRepo;
import TOPSECRET.domain.valueobject.CityId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoCityRepo implements ICityRepo {

    private final List<City> _cities = new ArrayList<>();

    @Override
    public City save(City city) {
        if (containsOfIdentity(city.identity())) {
            throw new IllegalStateException("City already exists for this country");
        }
        _cities.add(city);
        return city;
    }

    @Override
    public Iterable<City> findAll() {
        return List.copyOf(_cities);
    }

    @Override
    public Optional<City> ofIdentity(CityId cityId) {
        return _cities.stream()
                .filter(c -> c.identity().equals(cityId))
                .findFirst();
    }

    @Override
    public boolean containsOfIdentity(CityId cityId) {
        return _cities.stream()
                .anyMatch(c -> c.identity().equals(cityId));
    }
}