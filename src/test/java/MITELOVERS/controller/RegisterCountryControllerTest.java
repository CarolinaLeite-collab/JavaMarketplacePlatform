package MITELOVERS.controller;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.repository.ICountryRepo;
import MITELOVERS.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@WebMvcTest(RegisterCountryController.class)
@ActiveProfiles("jpa")
class RegisterCountryControllerTest {

    @MockBean
    ICountryRepo _iCountryRepoDouble;

    @MockBean
    CountryFactory _countryFactoryDouble;

    @Autowired
    RegisterCountryController _registerCountryController;

    private Country _countryDouble;
    private CountryId _countryIdDouble;

    @BeforeEach
    void setUp() throws InstantiationException {


        MockitoAnnotations.openMocks(this);
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
        verify(_countryFactoryDouble).createCountry(countryName);
        verify(_iCountryRepoDouble).save(_countryDouble);
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
