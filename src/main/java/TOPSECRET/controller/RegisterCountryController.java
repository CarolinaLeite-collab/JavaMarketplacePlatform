package TOPSECRET.controller;

import TOPSECRET.domain.Country;
import TOPSECRET.domain.CountryRepo;
import TOPSECRET.domain.User;

import java.time.LocalDate;

public class RegisterCountryController {
    private final CountryRepo _countryRepo;

    public RegisterCountryController(CountryRepo countryRepo) {
        _countryRepo = countryRepo;
    }

    public Country registerCountry(String countryName, User admin) {
        return _countryRepo.registerCountry(countryName, admin, LocalDate.now());
    }

}
