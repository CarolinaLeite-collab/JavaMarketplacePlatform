package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing {@link City} instances.
 * <p>
 * Provides methods to check for the existence of a city by name and country,
 * save new cities, and retrieve all stored cities as list.copyOf.
 * </p>
 */

public class MemoCityRepo implements ICityRepo {
    private final List<City> _cities;
    private final CityFactory _cityFactory;

    public MemoCityRepo(CityFactory cityFactory) {
        _cities = new ArrayList<>();
        _cityFactory = cityFactory;
    }

    @Override
    public City registerCity(String cityName, Country country) {
        if (existsCityInACountry(cityName, country)) {
            throw new IllegalStateException("City already exists for this country");
        }

        City city = _cityFactory.createCity(cityName, country);
        _cities.add(city);
        return city;
    }


    public boolean existsCityInACountry(String cityName, Country country) {
        if (cityName == null || country == null) {
            return false;
        }
        String normalized = cityName.trim();

        for (City city : _cities) {
            if (city.getName().equalsIgnoreCase(normalized) && city.getCountry().equals(country)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<City> getAllCities() {
        return List.copyOf(_cities);
    }

}
