package MITELOVERS.controller;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.repository.ICountryRepo;
import org.springframework.stereotype.Controller;

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
@Controller
public class RegisterCountryController {
    private final ICountryRepo _iCountryRepo;
    private final CountryFactory _countryFactory;

    public RegisterCountryController(ICountryRepo iCountryRepo, CountryFactory countryFactory) {
        _iCountryRepo = Objects.requireNonNull(iCountryRepo, "Country repository is required");
        _countryFactory = Objects.requireNonNull(countryFactory, "Country factory is required");
    }

    public Country registerCountry(String countryName) {
        Country country = _countryFactory.createCountry(countryName);

        if (_iCountryRepo.containsOfIdentity(country.identity())) {
            throw new IllegalArgumentException("Country already exists in the repository");
        }

        return _iCountryRepo.save(country);
    }
}
