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

    public RegisterCityController(CityRepo cityRepo, CountryRepo countryRepo) {
        _cityRepo = cityRepo;
        _countryRepo = countryRepo;
    }

    public List<Country> getCountries() {
        return _countryRepo.getAllCountries();
    }

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
