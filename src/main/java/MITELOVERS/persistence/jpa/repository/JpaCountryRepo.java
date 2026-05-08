package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.persistence.jpa.assembler.CountryAssembler;
import MITELOVERS.persistence.jpa.datamodel.CountryDataModel;
import MITELOVERS.persistence.jpa.springdata.ICountrySpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of {@link ICountryRepo} for storing {@link Country} instances.
 * <p>
 * Active only when the {@code jpa} Spring profile is enabled.
 * </p>
 */

@Repository
@Profile("jpa")
public class JpaCountryRepo implements ICountryRepo {

    @Autowired
    ICountrySpringDataRepo _iCountrySpringDataRepo;

    @Autowired
    CountryAssembler _assembler;

    @Override
    public Country save(Country country) {

        CountryDataModel dataModel = _assembler.toDataModel(country);
        CountryDataModel savedDM = _iCountrySpringDataRepo.save(dataModel);

        return _assembler.toDomain(savedDM);
    }

    @Override
    public Iterable<Country> findAll() {
        Iterable<CountryDataModel> countryDataModels = _iCountrySpringDataRepo.findAll();
        List<Country> countries = new ArrayList<>();
        for (CountryDataModel countryDataModel : countryDataModels) {
            countries.add(_assembler.toDomain(countryDataModel));
        }
        return countries;
    }

    @Override
    public List<CountryId> findAllKeys() {
        List<CountryId> keys = new ArrayList<>();
        for (CountryDataModel dm : _iCountrySpringDataRepo.findAll()) {
            keys.add(new CountryId(dm.getCountryId()));
        }
        return keys;
    }

    @Override
    public Optional<Country> ofIdentity(CountryId countryId) {
        CountryDataModel savedDM = _iCountrySpringDataRepo.findById(countryId.toString())
                .orElseThrow(() -> new IllegalArgumentException("Country not found!"));
        return Optional.of(_assembler.toDomain(savedDM));
    }

    @Override
    public boolean containsOfIdentity(CountryId countryId) {

        return _iCountrySpringDataRepo.existsById(countryId.toString());
    }

}