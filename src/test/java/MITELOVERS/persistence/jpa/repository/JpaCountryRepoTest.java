package MITELOVERS.persistence.jpa.repository;


import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.persistence.jpa.assembler.CountryAssembler;
import MITELOVERS.persistence.jpa.datamodel.CountryDataModel;
import MITELOVERS.persistence.jpa.springdata.ICountrySpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaCountryRepoTest {

    // SUT
    @InjectMocks
    private JpaCountryRepo jpaCountryRepo;

    @Mock
    private ICountrySpringDataRepo _springRepoDouble;

    @Mock
    private CountryAssembler _assemblerDouble;

    @Mock
    private Country _countryDouble;

    @Mock
    private CountryDataModel _dataModelDouble;


    @Test
    void testSaveShouldReturnDomainCountry() {
        // Arrange
        when(_assemblerDouble.toDataModel(_countryDouble)).thenReturn(_dataModelDouble);
        when(_springRepoDouble.save(_dataModelDouble)).thenReturn(_dataModelDouble);
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_countryDouble);

        // Act
        Country result = jpaCountryRepo.save(_countryDouble);

        // Assert
        assertEquals(_countryDouble, result);
    }

    @Test
    void testFindAllShouldReturnAllSavedCountries() {
        // Arrange
        when(_springRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_countryDouble);

        // Act
        Iterable<Country> result = jpaCountryRepo.findAll();
        List<Country> resultList = new ArrayList<>();
        for (Country country : result) {
            resultList.add(country);
        }

        // Assert
        assertEquals(1, resultList.size());
        assertEquals(_countryDouble, resultList.get(0));
    }

    @Test
    void testFindAllKeysShouldReturnListOfIds() {
        // Arrange
        when(_springRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_dataModelDouble.getCountryId()).thenReturn("PT");

        // Act
        List<CountryId> result = jpaCountryRepo.findAllKeys();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void testOfIdentityShouldReturnCountryOfACertainId() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);
        when(_springRepoDouble.findById(countryIdDouble.toString())).thenReturn(Optional.of(_dataModelDouble));
        when(_assemblerDouble.toDomain(_dataModelDouble)).thenReturn(_countryDouble);

        // Act
        Optional<Country> result = jpaCountryRepo.ofIdentity(countryIdDouble);

        // Assert
        assertEquals(_countryDouble, result.get());
    }

    @Test
    void testOfIdentityShouldThrowWhenNotFound() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);
        when(_springRepoDouble.findById(countryIdDouble.toString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            jpaCountryRepo.ofIdentity(countryIdDouble);
        });
    }

    @Test
    void testContainsOfIdentityShouldReturnTrueWhenCountryExists() {
        // Arrange
        CountryId countryIdDouble = mock(CountryId.class);
        when(countryIdDouble.toString()).thenReturn("PT");
        when(_springRepoDouble.existsById(countryIdDouble.toString())).thenReturn(true);

        // Act
        boolean result = jpaCountryRepo.containsOfIdentity(countryIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void testContainsOfIdentityShouldReturnFalseWhenCountryDoesNotExist() {
        // Arrange
        CountryId otherCountryIdDouble = mock(CountryId.class);
        when(otherCountryIdDouble.toString()).thenReturn("PT");

        // Act
        boolean result = jpaCountryRepo.containsOfIdentity(otherCountryIdDouble);

        // Assert
        assertFalse(result);
    }

}