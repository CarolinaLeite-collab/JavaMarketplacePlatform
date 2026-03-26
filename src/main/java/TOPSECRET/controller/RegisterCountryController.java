package TOPSECRET.controller;

import TOPSECRET.domain.*;


/**
 * Controller responsible for handling the country registration use case.
 * <p>
 * This controller acts as an intermediary between the user interface and the
 * Country repository, delegating the responsibility of creating and storing
 * countries to {@link ICountryRepo}.
 * </p>
 */

public class RegisterCountryController {
    private final ICountryRepo _iCountryRepo;

    public RegisterCountryController(ICountryRepo iCountryRepo, User _admin) {

        if (!_admin.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register countries");
        }

        _countryRepo = CountryRepo;
    }

    public Country registerCountry(String countryName) throws InstantiationException {

        return _countryRepo.registerCountry(countryName);
    }
}