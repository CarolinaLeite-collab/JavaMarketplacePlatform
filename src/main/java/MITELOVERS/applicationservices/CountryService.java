package MITELOVERS.applicationservices;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.dto.CountryDTO;
import MITELOVERS.mapper.CountryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class CountryService {

    private final ICountryRepo _countryRepo;
    private final CountryFactory _countryFactory;
    private final CountryMapper _mapper;

    public CountryService(ICountryRepo countryRepo,
                          CountryFactory countryFactory,
                          CountryMapper mapper) {

        _countryRepo = Objects.requireNonNull(countryRepo);
        _countryFactory = Objects.requireNonNull(countryFactory);
        _mapper = Objects.requireNonNull(mapper);
    }

    @Transactional
    public CountryDTO createCountry(String name) {
        Country country = _countryFactory.createCountry(name);

        if (_countryRepo.containsOfIdentity(country.identity())) {
            throw new IllegalArgumentException("Country already exists");
        }

        Country saved = _countryRepo.save(country);
        return _mapper.toDTO(saved);
    }

//    public CountryDTO updateCountry(String id, String newName) {
//        Country country = _countryRepo.ofIdentity(new CountryId(id))
//                .orElseThrow(NoSuchElementException::new);
//
//        country.rename(newName); // or whatever your domain method is
//
//        Country saved = _countryRepo.save(country);
//        return _mapper.toDTO(saved);
//    }
//
//    public void deleteCountry(String id) {
//        CountryId countryId = new CountryId(id);
//
//        if (!_countryRepo.containsOfIdentity(countryId)) {
//
//            throw new NoSuchElementException("Country not found");
//        }
//
//        _countryRepo.deleteById(countryId);
//    }


    public List<CountryDTO> listAllCountries() {
        return StreamSupport.stream(_countryRepo.findAll().spliterator(), false)
                .map(_mapper::toDTO)
                .toList();
    }

    public CountryDTO findById(String id) {
        Country country = _countryRepo.ofIdentity(new CountryId(id))
                .orElseThrow(() -> new NoSuchElementException("Country not found"));

        return _mapper.toDTO(country);
    }
}
