package MITELOVERS.applicationservices;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private ICountryRepo _countryRepoDouble;

    @Mock
    private CountryFactory _factoryDouble;

    @InjectMocks
    private CountryService _service;

    @Test
    void createCountry_whenCountryDoesNotExist_savesAndReturnsCountry() {
        // Arrange
        CountryId id = mock(CountryId.class);
        Country countryDouble = mock(Country.class);

        when(_factoryDouble.createCountry("Portugal")).thenReturn(countryDouble);
        when(countryDouble.identity()).thenReturn(id);
        when(_countryRepoDouble.containsOfIdentity(id)).thenReturn(false);
        when(_countryRepoDouble.save(countryDouble)).thenReturn(countryDouble);

        // Act
        Country result = _service.createCountry("Portugal");

        // Assert
        assertSame(countryDouble, result);
    }

    @Test
    void createCountry_whenCountryAlreadyExists_throwsException() {
        // Arrange
        CountryId id = mock(CountryId.class);
        Country countryDouble = mock(Country.class);

        when(_factoryDouble.createCountry("Portugal")).thenReturn(countryDouble);
        when(countryDouble.identity()).thenReturn(id);
        when(_countryRepoDouble.containsOfIdentity(id)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> _service.createCountry("Portugal"));
    }

    @Test
    void listAllCountries_returnsIterableFromRepo() {
        // Arrange
        Iterable<Country> expected = List.of(mock(Country.class));
        when(_countryRepoDouble.findAll()).thenReturn(expected);

        // Act
        Iterable<Country> result = _service.listAllCountries();

        // Assert
        assertSame(expected, result);
    }

    @Test
    void findById_whenCountryExists_returnsCountry() {
        // Arrange
        CountryId id = new CountryId("PT");
        Country countryDouble = mock(Country.class);

        when(_countryRepoDouble.ofIdentity(id)).thenReturn(Optional.of(countryDouble));

        // Act
        Country result = _service.findById("PT");

        // Assert
        assertSame(countryDouble, result);
    }

    @Test
    void findById_whenCountryDoesNotExist_throwsException() {
        // Arrange
        CountryId id = new CountryId("PT");
        when(_countryRepoDouble.ofIdentity(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findById("PT"));
    }

}