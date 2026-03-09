package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;
import java.util.Objects;

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
     * @param cityRepo repository where new cities will be registered
     * @param countryRepo repository used to validate and list countries
     */
    public RegisterCityController(CityRepo cityRepo, CountryRepo countryRepo) {
        _cityRepo = Objects.requireNonNull(cityRepo, "CityRepo cannot be null");
        _countryRepo = Objects.requireNonNull(countryRepo, "CountryRepo cannot be null");
    }

    /**
     * Returns all countries currently known to the application.
     */
    public List<Country> getCountries() {
        return _countryRepo.getAllCountries();
    }

    /**
     * Registers a new city in the system.
     *
     * @param name city name
     * @param country country to which the city belongs
     * @return the registered city
     */
    public City registerCity(String name, Country country) {
        return _cityRepo.add(name, country);
    }
}
