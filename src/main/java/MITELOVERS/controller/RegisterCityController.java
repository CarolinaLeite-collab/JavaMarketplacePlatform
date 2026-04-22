package MITELOVERS.controller;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.city.CityFactory;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.repository.ICityRepo;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.domain.valueobject.UserId;

/**
 * Controller responsible for handling the registration of a new {@link City} (US008).
 *
 * <p>Orchestrates: country existence check → aggregate creation via factory
 * → duplicate check → persistence via repository.</p>
 */

public class RegisterCityController {

    private final ICityRepo _iCityRepo;
    private final ICountryRepo _iCountryRepo;
    private final CityFactory _cityFactory;


    public RegisterCityController(ICityRepo iCityRepo,
                                  ICountryRepo iCountryRepo,
                                  CityFactory cityFactory,
                                  UserId adminId) {
        _iCityRepo = iCityRepo;
        _iCountryRepo = iCountryRepo;
        _cityFactory = cityFactory;
    }


    public City registerCity(String cityName, CountryId countryId) {
        _iCountryRepo.ofIdentity(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found"));

        City city = _cityFactory.createCity(cityName, countryId);

        if (_iCityRepo.containsOfIdentity(city.identity())) {
            throw new IllegalStateException("City already exists for this country");
        }

        return _iCityRepo.save(city);
    }

}