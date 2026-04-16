package TOPSECRET.controller;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for handling the registration of new countries.
 * <p>
 * This controller acts as an application layer entry point that delegates the
 * registration and persistence of a {@link Country} to the {@link ICountryRepo}.
 * </p>
 *
 * <p>
 * It coordinates the request between the domain and persistence layers to ensure
 * the country is correctly added to the system.
 * </p>
 */
public class RegisterCountryController {
    private final ICountryRepo _iCountryRepo;

    public RegisterCountryController(ICountryRepo iCountryRepo, UserId userAdmin) {
        _iCountryRepo = iCountryRepo;
    }

    public Country registerCountry(String countryName) {

        return _iCountryRepo.addCountry(countryName);
    }
}