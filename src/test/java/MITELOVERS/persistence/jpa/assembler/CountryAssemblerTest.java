package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.domain.valueobject.CountryName;
import MITELOVERS.persistence.jpa.datamodel.CountryDataModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountryAssemblerTest {

    @Test
    void toDataModelShouldMapIdFromCountryIdentity() {
        // Arrange
        CountryFactory factoryDouble = mock(CountryFactory.class);

        // SUT
        CountryAssembler assembler = new CountryAssembler(factoryDouble);

        Country countryDouble = mock(Country.class);
        when(countryDouble.identity()).thenReturn(new CountryId("PT"));
        when(countryDouble.name()).thenReturn(new CountryName("Portugal"));

        // Act
        CountryDataModel result = assembler.toDataModel(countryDouble);

        // Assert
        assertEquals("PT", result.getCountryId());
    }

    @Test
    void toDataModelShouldMapNameCorrectly() {
        // Arrange
        CountryFactory factoryDouble = mock(CountryFactory.class);

        // SUT
        CountryAssembler assembler = new CountryAssembler(factoryDouble);

        Country countryDouble = mock(Country.class);
        when(countryDouble.identity()).thenReturn(new CountryId("PT"));
        when(countryDouble.name()).thenReturn(new CountryName("Portugal"));

        // Act
        CountryDataModel result = assembler.toDataModel(countryDouble);

        // Assert
        assertEquals("PORTUGAL", result.getCountryName());
    }

    @Test
    void toDataModelShouldThrowWhenCountryIsNull() {
        // Arrange
        CountryFactory factoryDouble = mock(CountryFactory.class);

        // SUT
        CountryAssembler assembler = new CountryAssembler(factoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> assembler.toDataModel(null));
    }

    @Test
    void toDomainShouldDelegateToFactory() {
        // Arrange
        CountryFactory factoryDouble = mock(CountryFactory.class);

        // SUT
        CountryAssembler assembler = new CountryAssembler(factoryDouble);

        CountryDataModel dmDouble = mock(CountryDataModel.class);
        Country countryDouble = mock(Country.class);

        when(dmDouble.getCountryId()).thenReturn("PT");
        when(dmDouble.getCountryName()).thenReturn("PORTUGAL");
        when(factoryDouble.createCountry(any(CountryId.class), anyString())).thenReturn(countryDouble);

        // Act
        Country result = assembler.toDomain(dmDouble);

        // Assert
        assertSame(countryDouble, result);
    }

    @Test
    void toDomainShouldThrowWhenDataModelIsNull() {
        // Arrange
        CountryFactory factoryDouble = mock(CountryFactory.class);

        // SUT
        CountryAssembler assembler = new CountryAssembler(factoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> assembler.toDomain(null));
    }
}
