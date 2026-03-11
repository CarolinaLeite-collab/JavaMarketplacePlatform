package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Repository for managing {@link City} instances.
 * <p>
 * Provides methods to check for the existence of a city by name and country,
 * save new cities, and retrieve all stored cities as an unmodifiable list.
 * </p>
 */

public class CityRepo {
    private final List<City> _cities;
    private final CityFactory _cityFactory;

    public CityRepo(CityFactory cityFactory) {
        _cities = new ArrayList<>();
        _cityFactory = Objects.requireNonNull(cityFactory, "CityFactory cannot be null");
    }

    public boolean existsByNameAndCountry(String name, Country country) {
        if (name == null || country == null) {
            return false;
        }
        String normalized = name.trim();

        for (City city : _cities) {
            if (city.getName().equalsIgnoreCase(normalized) && city.getCountry().equals(country)) {
                return true;
            }
        }
        return false;
    }

    public City add(String name, Country country) {
        if (existsByNameAndCountry(name, country)) {
            throw new IllegalStateException("City already exists for this country");
        }

        City city = _cityFactory.createCity(name, country);
        _cities.add(city);
        return city;
    }

    public List<City> getAll() {
        return Collections.unmodifiableList(_cities);
    }
}
