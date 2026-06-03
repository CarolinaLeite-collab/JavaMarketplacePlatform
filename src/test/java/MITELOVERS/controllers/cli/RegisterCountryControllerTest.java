package MITELOVERS.controllers.cli;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class RegisterCountryControllerTest {

    @Mock
    ICountryRepo _iCountryRepoDouble;

    @Mock
    CountryFactory _countryFactoryDouble;

    @InjectMocks
    RegisterCountryController _registerCountryController;

    private Country _countryDouble;
    private CountryId _countryIdDouble;

    @BeforeEach
    void setUp() throws InstantiationException {

        _countryDouble = mock(Country.class);
        _countryIdDouble = mock(CountryId.class);

    }

    @Test
    void registerCountryShouldCreateAndReturnCountry() {
        // Arrange
        String countryName = "Portugal";
        when(_countryFactoryDouble.createCountry(countryName)).thenReturn(_countryDouble);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);
        when(_iCountryRepoDouble.containsOfIdentity(_countryIdDouble)).thenReturn(false);
        when(_iCountryRepoDouble.save(_countryDouble)).thenReturn(_countryDouble);

        // Act
        Country result = _registerCountryController.registerCountry(countryName);

        // Assert
        assertEquals(_countryDouble, result);
    }

    @Test
    void registerCountryShouldThrowWhenCountryAlreadyExists() {
        // Arrange
        when(_countryFactoryDouble.createCountry("Portugal")).thenReturn(_countryDouble);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);
        when(_iCountryRepoDouble.containsOfIdentity(_countryIdDouble)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> _registerCountryController.registerCountry("Portugal"));
    }

    @Test
    void registerCountryShouldThrowCorrectMessageWhenCountryAlreadyExists() {
        // Arrange
        when(_countryFactoryDouble.createCountry("Portugal")).thenReturn(_countryDouble);
        when(_countryDouble.identity()).thenReturn(_countryIdDouble);
        when(_iCountryRepoDouble.containsOfIdentity(_countryIdDouble)).thenReturn(true);

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> _registerCountryController.registerCountry("Portugal"));

        // Assert
        assertEquals("Country already exists in the repository", ex.getMessage());
    }
}
