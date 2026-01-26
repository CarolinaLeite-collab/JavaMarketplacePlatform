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

    /**
     * Checks if a city already exists under the provided name and country.
     *
     * @param name city name, case is ignored
     * @param country country to which the city belongs
     * @return {@code true} when a matching city exists, {@code false} otherwise
     */
    public boolean existsByNameAndCountry(String name, Country country) {
        // Treat null name or country as "no match" (don't throw) so callers can safely query without null-checking
        if (name == null || country == null) return false;

        String normalized = name.trim();

        for (City c : _cities) {
            if (c.getName().equalsIgnoreCase(normalized) && c.getCountry().equals(country)) return true;
        }
        return false;
    }

    /**
     * Saves a city in the repository when it does not yet exist.
     *
     * @param city city to persist
     * @return the saved city or {@code null} when a duplicate was detected
     */
    public City save(City city) {
        if (city == null) throw new IllegalArgumentException("City cannot be null");
        if (existsByNameAndCountry(city.getName(), city.getCountry())) return null;

        _cities.add(city);
        return city;
    }

    /**
     * Returns an unmodifiable view of all cities stored so far.
     */
    public List<City> getAll() {
        return Collections.unmodifiableList(_cities);
    }
}
