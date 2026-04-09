package TOPSECRET.controller;

import TOPSECRET.domain.user.User;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.Role;

/**
 * Controller responsible for handling the country registration use case.
 */
public class RegisterCountryController {
    private final ICountryRepo _iCountryRepo;

    public RegisterCountryController(ICountryRepo iCountryRepo) {
        _iCountryRepo = iCountryRepo;
    }

    public Country registerCountry(User user, String countryName) {
        if (!user.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register countries");
        }

        return _iCountryRepo.addCountry(countryName);
    }
}