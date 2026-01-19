package TOPSECRET.controller;

import TOPSECRET.domain.City;
import TOPSECRET.domain.CityRepo;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.CountryRepo;

import java.time.LocalDate;

public class CityRegisterController {
    private final CityRepo _cityRepo;
    private final CountryRepo _countryRepo;

    public CityRegisterController(CityRepo cityRepo, CountryRepo countryRepo) {
        _cityRepo = cityRepo;
        _countryRepo = countryRepo;
    }

    public City registerCity(String name, String countryName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (countryName == null || countryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be null or blank");
        }

        // find country by name in CountryRepo
        Country country = _countryRepo.findByName(countryName);

        if (country == null) {
            throw new IllegalArgumentException("Country not found");
        }

        if (_cityRepo.existsByNameAndCountry(name.trim(), country)) {
            throw new IllegalStateException("City already exists in country");
        }

        City city = new City(name, country, LocalDate.now());
        return _cityRepo.save(city);
    }
}
