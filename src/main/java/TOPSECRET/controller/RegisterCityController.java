package TOPSECRET.controller;

import TOPSECRET.domain.city.City;
import TOPSECRET.domain.city.CityFactory;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.repository.ICityRepo;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for handling the registration of a new {@link City} in the system.
 * <p>
 * This controller acts as an application layer entry point that delegates the creation
 * and persistence of a {@link City} to the {@link ICityRepo}.
 * </p>
 *
 * <p>
 * It ensures that a city can be registered under a given {@link CountryId}, coordinating
 * the request between the domain and persistence layers.
 * </p>
 */

public class RegisterCityController {

    private final ICityRepo _iCityRepo;
    private final ICountryRepo _iCountryRepo;
    private final CityFactory _cityFactory;

    public RegisterCityController(ICityRepo iCityRepo, ICountryRepo iCountryRepo, CityFactory cityFactory, UserId adminId) {

        _iCityRepo = iCityRepo;
        _iCountryRepo = iCountryRepo;
        _cityFactory = cityFactory;
    }


    public City registerCity(String cityName, CountryId countryId) {
        Country country = _iCountryRepo.ofIdentity(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found"));

        City city = _cityFactory.createCity(cityName, countryId);
        return _iCityRepo.addCity(city);
    }

}