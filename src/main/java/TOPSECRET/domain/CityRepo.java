package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CityRepo {
    private final List<City> _cities = new ArrayList<>();

    public boolean existsByNameAndCountry(String name, Country country) {
        if (name == null || country == null) return false;
        for (City c : _cities) {
            if (c.getName().equals(name) && c.getCountry().equals(country)) return true;
        }
        return false;
    }

    public City save(City city) {
        if (city == null) throw new IllegalArgumentException("City cannot be null");
        if (existsByNameAndCountry(city.getName(), city.getCountry())) return null;
        _cities.add(city);
        return city;
    }

    public List<City> getAll() {
        return Collections.unmodifiableList(_cities);
    }
}
