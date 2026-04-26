package MITELOVERS.persistence.mem;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.repository.ICityRepo;
import MITELOVERS.domain.valueobject.CityId;

import java.util.*;

public class MemCityRepo implements ICityRepo {

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

    @Override
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
