package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.valueobject.CountryId;

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

        if (!admin.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register cities");
        }
        _iCityRepo = iCityRepo;
        _iCountryRepo = iCountryRepo;
    }

    public java.util.List<Country> getAllCountries() {
        return _iCountryRepo.getAllCountries();
    }

    public City registerCity(String cityName, CountryId countryId) {
        Country country = _iCountryRepo.ofIdentity(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found"));
        return _iCityRepo.registerCity(cityName, country);
    }

    public City registerCity(String cityName, Country country) {
        return registerCity(cityName, country.identity());
    }
}
