package TOPSECRET.controller;

import TOPSECRET.domain.City;
import TOPSECRET.domain.CityRepo;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.CountryRepo;

import java.util.List;

/**
 * Controller responsible for registering new cities in the system.
 * <p>
 * This controller interacts with {@link CityRepo} and {@link CountryRepo} to
 * retrieve available countries and to register new {@link City} instances,
 * ensuring that city names are valid and unique within a given country.
 * </p>
 */

public class RegisterCityController {
    private final CityRepo _cityRepo;
    private final CountryRepo _countryRepo;

    /**
     * Creates a controller with the repositories required to register cities.
     *
     * @param cityRepo repository where new cities will be stored
     * @param countryRepo repository used to validate and list countries
     */
    public RegisterCityController(CityRepo cityRepo, CountryRepo countryRepo) {
        _cityRepo = cityRepo;
        _countryRepo = countryRepo;
    }

    /**
     * Returns all countries currently known to the application.
     */
    public List<Country> getCountries() {
        return _countryRepo.getAllCountries();
    }

    /**
     * Normalizes the provided name, validates it against duplicates, and registers a new city.
     *
     * @param name display name of the city to register
     * @param country parent country of the city
     * @return the saved {@link City} instance
     * @throws IllegalArgumentException when {@code name} is missing or {@code country} is null
     * @throws IllegalStateException when a city with the same normalized name already exists in the country
     */
    public City registerCity(String name, Country country) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }

        String normalizedName = name.trim();
        if (_cityRepo.existsByNameAndCountry(normalizedName, country)) {
            throw new IllegalStateException("City already exists for this country");
        }

        City city = new City(normalizedName, country);
        return _cityRepo.save(city);
    }
}
