package TOPSECRET.controller;

import TOPSECRET.domain.Country;
import TOPSECRET.domain.ICountryRepo;
import TOPSECRET.domain.Role;
import TOPSECRET.domain.User;


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

    public RegisterCountryController(ICountryRepo iCountryRepo) {

        _iCountryRepo = iCountryRepo;
    }

    public Country registerCountry(User user,String countryName) {

        if (!user.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register countries");
        }
        return _iCountryRepo.registerCountry(countryName);
    }
}