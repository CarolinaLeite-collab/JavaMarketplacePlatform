package TOPSECRET.controller;

import TOPSECRET.domain.City;
import TOPSECRET.domain.CityRepo;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.CountryRepo;

public class CityRegisterController {
    private final CityRepo _cityRepo;
    private final CountryRepo _countryRepo;

    public CityRegisterController(CityRepo cityRepo, CountryRepo countryRepo) {
        _cityRepo = cityRepo;
        _countryRepo = countryRepo;
    }

    public City registerCity(String name, String countryId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (countryId == null || countryId.trim().isEmpty()) {
            throw new IllegalArgumentException("Country ID cannot be null or blank");
        }

        Country country = _countryRepo.findById(countryId);
        if (country == null) {
            throw new IllegalArgumentException("Country not found");
        }

        if (_cityRepo.existsByNameAndCountry(name.trim(), country)) {
            throw new IllegalStateException("City already exists for this country");
        }

        City city = new City(name, country);
        return _cityRepo.save(city);
    }
}
