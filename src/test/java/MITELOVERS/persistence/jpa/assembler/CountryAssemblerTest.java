package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.persistence.jpa.datamodel.CountryDataModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryAssemblerTest {

    @Test
    void domain2dmReturnsMappedDataModel() {
        // Arrange
        Country country = new CountryFactory().createCountry("Portugal");

        // SUT
        CountryAssembler countryAssembler = new CountryAssembler();

        // Act
        CountryDataModel countryDataModel = countryAssembler.domain2dm(country);

        // Assert
        assertEquals("PT", countryDataModel.getCountryId());
        assertEquals("PORTUGAL", countryDataModel.getCountryName());
    }

    @Test
    void dm2domainReturnsMappedCountry() {
        // Arrange
        CountryDataModel countryDataModel = new CountryDataModel("PT", "PORTUGAL");

        // SUT
        CountryAssembler countryAssembler = new CountryAssembler();

        // Act
        Country country = countryAssembler.dm2domain(countryDataModel);

        // Assert
        assertEquals("PT", country.identity().toString());
        assertEquals("PORTUGAL", country.name().toString());
    }
}