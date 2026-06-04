package MITELOVERS.mapper;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.domain.valueobject.CountryName;
import MITELOVERS.dto.response.CountryResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryResponseDTOMapperTest {

    private CountryResponseDTOMapper _mapper;

    @BeforeEach
    void setUp() {
        _mapper = new CountryResponseDTOMapper();
    }

    @Test
    void toModel_returnsDTOWithCorrectFields() {
        // Arrange
        CountryId id = mock(CountryId.class);
        CountryName name = mock(CountryName.class);

        when(id.toString()).thenReturn("123");
        when(name.toString()).thenReturn("Portugal");

        Country countryDouble = mock(Country.class);
        when(countryDouble.identity()).thenReturn(id);
        when(countryDouble.name()).thenReturn(name);

        // Act
        CountryResponseDTO result = _mapper.toModel(countryDouble);

        // Assert
        assertEquals("123", result.getCountryId());
        assertEquals("Portugal", result.getName());
    }

    @Test
    void toModel_doesNotAddLinksOrActions() {
        // Arrange
        CountryId id = mock(CountryId.class);
        CountryName name = mock(CountryName.class);

        when(id.toString()).thenReturn("1");
        when(name.toString()).thenReturn("Spain");

        Country countryDouble = mock(Country.class);
        when(countryDouble.identity()).thenReturn(id);
        when(countryDouble.name()).thenReturn(name);

        // Act
        CountryResponseDTO result = _mapper.toModel(countryDouble);

        // Assert
        assertTrue(result.getLinks().isEmpty());
    }

}