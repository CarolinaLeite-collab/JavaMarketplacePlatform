package TOPSECRET.controller;

import TOPSECRET.domain.*;
import java.util.List;

/**
 * Controller responsible for registering new cities in the system.
 * <p>
 * This controller interacts with {@link ICityRepo} and {@link ICountryRepo} to
 * retrieve available countries and to register new {@link City} instances,
 * ensuring that city names are valid and unique within a given country.
 * </p>
 */

public class RegisterCityController {
    private final ICityRepo _iCityRepo;
    private final ICountryRepo _iCountryRepo;

    public RegisterCityController(ICityRepo iCityRepo, ICountryRepo iCountryRepo, User admin) {
        _iCityRepo = iCityRepo;
        _iCountryRepo = iCountryRepo;
    }

    public List<Country> getAllCountries() {
        return _countryRepo.getAllCountries();
    }

    public City registerCity(String cityName, Country country) {
        return _iCityRepo.registerCity(cityName, country);
    }

}
