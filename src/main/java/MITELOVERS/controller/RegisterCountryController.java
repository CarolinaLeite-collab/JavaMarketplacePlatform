package MITELOVERS.controller;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.UserId;

import java.util.Objects;

/**
 * Controller responsible for handling the registration of new countries.
 * <p>
 * This controller acts as an application layer entry point that orchestrates
 * country creation, duplicate validation and persistence.
 * </p>
 *
 * <p>
 * It creates {@link Country} aggregates via {@link CountryFactory} and delegates
 * persistence to {@link ICountryRepo}.
 * </p>
 */
public class RegisterCountryController {
    private final ICountryRepo _iCountryRepo;
    private final CountryFactory _countryFactory;
    private final UserId _userAdmin;

    public RegisterCountryController(ICountryRepo iCountryRepo, CountryFactory countryFactory, UserId userAdmin) {
        _iCountryRepo = Objects.requireNonNull(iCountryRepo, "Country repository is required");
        _countryFactory = Objects.requireNonNull(countryFactory, "Country factory is required");
        _userAdmin = Objects.requireNonNull(userAdmin, "Admin user is required");
    }

    public Country registerCountry(String countryName) {
        Country country = _countryFactory.createCountry(countryName);

        if (_iCountryRepo.containsOfIdentity(country.identity())) {
            throw new IllegalArgumentException("Country already exists in the repository");
        }

        return _iCountryRepo.save(country);
    }
}
