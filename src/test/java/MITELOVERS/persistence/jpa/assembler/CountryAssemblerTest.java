package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.persistence.jpa.datamodel.CountryDataModel;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void domainList2dmListReturnsMappedList() {
        // Arrange
        CountryFactory factory = new CountryFactory();
        List<Country> countries = List.of(
                factory.createCountry("Portugal"),
                factory.createCountry("Spain"));

        // SUT
        CountryAssembler countryAssembler = new CountryAssembler();

        // Act
        List<CountryDataModel> result = countryAssembler.domainList2dmList(countries);

        // Assert
        assertEquals(2, result.size());
        assertEquals("PT", result.get(0).getCountryId());
        assertEquals("ES", result.get(1).getCountryId());
    }

    @Test
    void domainList2dmListEmptyListReturnsEmptyList() {
        // SUT
        CountryAssembler countryAssembler = new CountryAssembler();

        // Act
        List<CountryDataModel> result = countryAssembler.domainList2dmList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void dmList2DomainListReturnsMappedList() {
        // Arrange
        List<CountryDataModel> countryDataModels = List.of(
                new CountryDataModel("PT", "PORTUGAL"),
                new CountryDataModel("ES", "SPAIN"));

        // SUT
        CountryAssembler countryAssembler = new CountryAssembler();

        // Act
        List<Country> result = countryAssembler.dmList2DomainList(countryDataModels);

        // Assert
        assertEquals(2, result.size());
        assertEquals("PT", result.get(0).identity().toString());
        assertEquals("ES", result.get(1).identity().toString());
    }

    @Test
    void dmList2DomainListEmptyListReturnsEmptyList() {
        // SUT
        CountryAssembler countryAssembler = new CountryAssembler();

        // Act
        List<Country> result = countryAssembler.dmList2DomainList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }
}