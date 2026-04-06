package TOPSECRET.controller;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.country.CountryFactory;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;
import TOPSECRET.domain.valueobject.UserId;

import java.util.Optional;

/**
 * Controller responsible for handling the country registration use case.
 */
public class RegisterCountryController {
    private final ICountryRepo _iCountryRepo;
    private final CountryFactory _countryFactory;

    public RegisterCountryController(ICountryRepo iCountryRepo, CountryFactory countryFactory, UserId adminId) {
        _iCountryRepo = iCountryRepo;
        _countryFactory = countryFactory;
    }

    // Backwards compatible constructor used by legacy tests
//    public RegisterCountryController(ICountryRepo iCountryRepo) {
//        this(iCountryRepo, new CountryFactory());
//    }

    public Optional<Country> registerCountry(User user, String isoCode, String countryName) {
        if (!user.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register countries");
        }

        CountryId id = new CountryId(isoCode);
        CountryName name = new CountryName(countryName);

        // If a country with the same id already exists, return it
        if (_iCountryRepo.containsOfIdentity(id)) {
            return _iCountryRepo.ofIdentity(id);
        }

        Country country = _countryFactory.createCountry(id, name);
        Country saved = _iCountryRepo.save(country);
        return Optional.ofNullable(saved);
    }

    // Legacy compatibility: registerCountry(User, String)
    public Country registerCountry(User user, String countryName) {
        return registerCountry(user, null, countryName).orElse(null);
    }
}