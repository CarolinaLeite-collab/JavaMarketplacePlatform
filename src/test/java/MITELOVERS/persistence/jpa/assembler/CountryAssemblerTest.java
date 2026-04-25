package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.persistence.jpa.datamodel.CountryDM;
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
        CountryDM countryDM = countryAssembler.domain2dm(country);

        // Assert
        assertEquals("PT", countryDM.getCountryId());
        assertEquals("PORTUGAL", countryDM.getCountryName());
    }

    @Test
    void dm2domainReturnsMappedCountry() {
        // Arrange
        CountryDM countryDM = new CountryDM("PT", "PORTUGAL");

        // SUT
        CountryAssembler countryAssembler = new CountryAssembler();

        // Act
        Country country = countryAssembler.dm2domain(countryDM);

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
        List<CountryDM> result = countryAssembler.domainList2dmList(countries);

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
        List<CountryDM> result = countryAssembler.domainList2dmList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void dmList2DomainListReturnsMappedList() {
        // Arrange
        List<CountryDM> dms = List.of(
                new CountryDM("PT", "PORTUGAL"),
                new CountryDM("ES", "SPAIN"));

        // SUT
        CountryAssembler countryAssembler = new CountryAssembler();

        // Act
        List<Country> result = countryAssembler.dmList2DomainList(dms);

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