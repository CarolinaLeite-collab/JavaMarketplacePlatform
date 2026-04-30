package MITELOVERS.persistence.mem;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.repository.ICityRepo;
import MITELOVERS.domain.valueobject.CityId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile("mem")
public class MemCityRepo implements ICityRepo {

    private final Map<CityId, City> DATA = new HashMap<>();

    @Override
    public City save(City city) {
        DATA.put(city.identity(), city);
        return city;
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
