package TOPSECRET.controller;

import TOPSECRET.domain.Country;
import TOPSECRET.domain.CountryRepo;
import TOPSECRET.domain.Role;
import TOPSECRET.domain.User;

import java.util.Objects;

/**
 * Controller responsible for handling the country registration use case.
 * <p>
 * This controller acts as an intermediary between the user interface and the
 * Country repository, delegating the responsibility of creating and storing
 * countries to {@link CountryRepo}.
 * </p>
 */
public class RegisterCountryController {
    private final CountryRepo _countryRepo;

    public RegisterCountryController(CountryRepo countryRepo) {
        this._countryRepo = Objects.requireNonNull(countryRepo, "CountryRepo cannot be null");
    }

    public Country registerCountry(String countryName, User user) {
        Objects.requireNonNull(user, "User cannot be null");

        if (!user.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register countries");
        }
        return _countryRepo.registerCountry(countryName);
    }
}