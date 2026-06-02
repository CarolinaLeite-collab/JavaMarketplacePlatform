package MITELOVERS.applicationservices;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.dto.CountryDTO;
import MITELOVERS.mapper.CountryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @InjectMocks
    private CountryService _service;

    @Mock
    private ICountryRepo _repo;

    @Mock
    private CountryFactory _factory;

    @Mock
    private CountryMapper _mapper;

    @BeforeEach
    void setup() {

        _service = new CountryService(_repo, _factory, _mapper);
    }

    @Test
    void createCountry_returnsMappedDTO_whenCountryIsNew() {
        // arrange
        Country domain = mock(Country.class);
        CountryId id = mock(CountryId.class);
        Country saved = mock(Country.class);
        CountryDTO dto = mock(CountryDTO.class);

        when(_factory.createCountry("Portugal")).thenReturn(domain);
        when(domain.identity()).thenReturn(id);
        when(_repo.containsOfIdentity(id)).thenReturn(false);
        when(_repo.save(domain)).thenReturn(saved);
        when(_mapper.toDTO(saved)).thenReturn(dto);

        // act (SUT)
        CountryDTO result = _service.createCountry("Portugal");

        // assert
        assertSame(dto, result);
    }

    @Test
    void createCountry_throwsException_whenCountryAlreadyExists() {
        // arrange
        Country domain = mock(Country.class);
        CountryId id = mock(CountryId.class);

        when(_factory.createCountry("Portugal")).thenReturn(domain);
        when(domain.identity()).thenReturn(id);
        when(_repo.containsOfIdentity(id)).thenReturn(true);

        // act + assert
        assertThrows(IllegalArgumentException.class,
                () -> _service.createCountry("Portugal"));
    }

    @Test
    void listAllCountries_returnsMappedDTOs() {
        // arrange
        Country c1 = mock(Country.class);
        Country c2 = mock(Country.class);

        CountryDTO dto1 = mock(CountryDTO.class);
        CountryDTO dto2 = mock(CountryDTO.class);

        when(_repo.findAll()).thenReturn(List.of(c1, c2));
        when(_mapper.toDTO(c1)).thenReturn(dto1);
        when(_mapper.toDTO(c2)).thenReturn(dto2);

        // act (SUT)
        List<CountryDTO> result = _service.listAllCountries();

        // assert
        assertEquals(List.of(dto1, dto2), result);
    }

    @Test
    void findById_returnsMappedDTO_whenCountryExists() {
        // arrange
        Country domain = mock(Country.class);
        CountryDTO dto = mock(CountryDTO.class);

        when(_repo.ofIdentity(any(CountryId.class))).thenReturn(Optional.of(domain));
        when(_mapper.toDTO(domain)).thenReturn(dto);

        // act (SUT)
        CountryDTO result = _service.findById("PT");

        // assert
        assertSame(dto, result);
    }

    @Test
    void findById_throwsException_whenCountryDoesNotExist() {
        // arrange
        when(_repo.ofIdentity(any(CountryId.class))).thenReturn(Optional.empty());

        // act + assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findById("PT"));
    }

}