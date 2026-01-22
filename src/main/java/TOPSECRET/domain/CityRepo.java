package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repository for managing {@link City} instances.
 * <p>
 * Provides methods to check for the existence of a city by name and country,
 * save new cities, and retrieve all stored cities as an unmodifiable list.
 * </p>
 */

public class CityRepo {
    private final List<City> _cities = new ArrayList<>();

    public boolean existsByNameAndCountry(String name, Country country) {
        if (name == null || country == null) return false;
        String normalized = name.trim();
        for (City c : _cities) {
            if (c.getName().equalsIgnoreCase(normalized) && c.getCountry().equals(country)) return true;
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
