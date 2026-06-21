package MITELOVERS.applicationservices;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Application service responsible for country registration and retrieval.
 * <p>
 * Acts as the entry point between controllers and the country domain, delegating
 * creation to {@link CountryFactory} and persistence to {@link ICountryRepo}. Enforces
 * that a country cannot be created if one with the same identity already exists.
 * </p>
 */

@Service
public class CountryService {

    private final ICountryRepo _iCountryRepo;
    private final CountryFactory _factory;

    public CountryService(ICountryRepo repo, CountryFactory factory) {

        _iCountryRepo = Objects.requireNonNull(repo);
        _factory = Objects.requireNonNull(factory);
    }

    @Transactional
    public Country createCountry(String name) {

        Country country = _factory.createCountry(name);

        if (_iCountryRepo.containsOfIdentity(country.identity())) {
            throw new IllegalArgumentException("Country already exists");
        }

        return _iCountryRepo.save(country);
    }

    public Iterable<Country> listAllCountries() {
        return _iCountryRepo.findAll();
    }

    public Country findById(String id) {
        return _iCountryRepo.ofIdentity(new CountryId(id))
                .orElseThrow(() -> new NoSuchElementException("Country not found"));
    }

}
