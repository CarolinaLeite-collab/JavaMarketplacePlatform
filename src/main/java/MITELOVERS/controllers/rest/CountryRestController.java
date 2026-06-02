package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.CountryService;
import MITELOVERS.dto.CountryCollectionDTO;
import MITELOVERS.dto.CountryDTO;
import MITELOVERS.mapper.CountryCollectionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/countries")
public class CountryRestController {

    private final CountryService _service;
    private final CountryCollectionMapper _collectionMapper;

    public CountryRestController(CountryService service, CountryCollectionMapper collectionMapper) {
        _service = service;
        _collectionMapper = collectionMapper;
    }

    @PostMapping
    public CountryDTO create(@RequestBody String name) {

        try{

            CountryDTO countryDto = _service.createCountry(name);

            return new ResponseEntity<>(countryDto, HttpStatus.OK).getBody();

        } catch (IllegalArgumentException ex) {

            throw new ResponseStatusException(HttpStatus.CONFLICT);

        }
    }

//    @PutMapping("/{id}")
//    public CountryDTO update(@PathVariable String id, @RequestBody String newName) {
//
//        try {
//
//            return _service.updateCountry(id, newName);
//
//        } catch (NoSuchElementException ex) {
//
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable String id) {
//
//        try {
//
//            _service.deleteCountry(id);
//
//            return ResponseEntity.noContent().build();
//
//        } catch (NoSuchElementException ex) {
//
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping
    public ResponseEntity<CountryCollectionDTO> listAll() {

        List<CountryDTO> countries = _service.listAllCountries();

        CountryCollectionDTO collectionDto = _collectionMapper.toDTO(countries);

        return ResponseEntity.ok(collectionDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> findById(@PathVariable String id) {

        try {

            CountryDTO countryDto = _service.findById(id);

            return new ResponseEntity<>(countryDto, HttpStatus.OK);

        } catch (NoSuchElementException ex) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
