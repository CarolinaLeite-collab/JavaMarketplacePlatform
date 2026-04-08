package TOPSECRET.controller;

import TOPSECRET.domain.city.City;
import TOPSECRET.domain.city.CityFactory;
import TOPSECRET.domain.repository.ICityRepo;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.valueobject.CityId;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.Role;

import java.util.List;

public class RegisterCityController {

    private final ICityRepo _iCityRepo;
    private final ICountryRepo _iCountryRepo;
    private final CityFactory _cityFactory;

    public RegisterCityController(ICityRepo iCityRepo, ICountryRepo iCountryRepo, CityFactory cityFactory, User admin) {
        if (!admin.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register cities");
        }
        _iCityRepo = iCityRepo;
        _iCountryRepo = iCountryRepo;
        _cityFactory = cityFactory;
    }

    public Iterable<Country> getAllCountries() {
        return _iCountryRepo.findAll();
    }

    public City registerCity(String cityName, CountryId countryId) {
        Country country = _iCountryRepo.ofIdentity(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found"));

        CityId cityId = new CityId(cityName, countryId);

        if (_iCityRepo.containsOfIdentity(cityId)) {
            throw new IllegalStateException("City already exists for this country");
        }

        City city = _cityFactory.createCity(cityName, country);
        return _iCityRepo.save(city);
    }

    public City registerCity(String cityName, Country country) {
        return registerCity(cityName, country.identity());
    }
}