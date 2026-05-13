package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.persistence.jpa.assembler.CityAssembler;
import MITELOVERS.persistence.jpa.datamodel.CityDataModel;
import MITELOVERS.persistence.springdata.ICitySpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaCityRepoTest {

    @InjectMocks
    private JpaCityRepo jpaCityRepo;

    @Mock
    private ICitySpringDataRepo springDataRepo;

    @Mock
    private CityAssembler assembler;

    @Test
    void testJpaCityRepoConstructor() {
        assertNotNull(jpaCityRepo);
    }

    @Test
    void testSaveResultSavedCity() {
        //Arrange
        City cityDouble = mock(City.class);

        CityDataModel cityDMDouble = mock(CityDataModel.class);
        CityDataModel savedCityDMDouble = mock(CityDataModel.class);

        when(assembler.toDataModel(cityDouble)).thenReturn(cityDMDouble);
        when(springDataRepo.save(cityDMDouble)).thenReturn(savedCityDMDouble);
        when(assembler.toDomain(savedCityDMDouble)).thenReturn(cityDouble);

        //Act
        City result = jpaCityRepo.save(cityDouble);

        //Assert
        assertNotNull(result);
        assertEquals(cityDouble, result);
    }

    @Test
    void testFindAllReturnsCities() {
        // Arrange
        CityDataModel cityDM1 = mock(CityDataModel.class);
        CityDataModel cityDM2 = mock(CityDataModel.class);

        List<CityDataModel> dmList = List.of(cityDM1, cityDM2);

        City city1 = mock(City.class);
        City city2 = mock(City.class);

        List<City> cityList = List.of(city1, city2);

        when(springDataRepo.findAll()).thenReturn(dmList);
        when(assembler.toDomainList(dmList)).thenReturn(cityList);

        // Act
        Iterable<City> result = jpaCityRepo.findAll();

        // Assert
        assertEquals(cityList, result);
    }

    @Test
    void testFindAllKeysReturnsCityIds() {
        // Arrange
        CityDataModel cityDM = mock(CityDataModel.class);

        when(cityDM.getName()).thenReturn("Lisboa");
        when(cityDM.getCountryId()).thenReturn("PT");

        when(springDataRepo.findAll()).thenReturn(List.of(cityDM));

        // Act
        List<CityId> result = jpaCityRepo.findAllKeys();

        // Assert
        assertEquals(1, result.size());
        assertEquals("PTlisboa", result.get(0).toString());
    }

    @Test
    void testOfIdentityReturnsCityWhenFound() {
        // Arrange
        CountryId countryId = new CountryId("PT");
        CityId cityId = new CityId("Lisboa", countryId);

        CityDataModel cityDM = mock(CityDataModel.class);
        City city = mock(City.class);

        when(springDataRepo.findById(cityId.toString())).thenReturn(Optional.of(cityDM));
        when(assembler.toDomain(cityDM)).thenReturn(city);

        // Act
        Optional<City> result = jpaCityRepo.ofIdentity(cityId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(city, result.get());
    }

    @Test
    void testOfIdentityReturnsEmptyWhenNotFound() {
        // Arrange
        CountryId countryId = new CountryId("PT");
        CityId cityId = new CityId("Lisboa", countryId);

        when(springDataRepo.findById(cityId.toString())).thenReturn(Optional.empty());

        // Act
        Optional<City> result = jpaCityRepo.ofIdentity(cityId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testContainsOfIdentityReturnsTrueWhenExists() {
        // Arrange
        CountryId countryId = new CountryId("PT");
        CityId cityId = new CityId("Lisboa", countryId);

        when(springDataRepo.existsById(cityId.toString())).thenReturn(true);

        // Act
        boolean result = jpaCityRepo.containsOfIdentity(cityId);

        // Assert
        assertTrue(result);
    }

    @Test
    void testContainsOfIdentityReturnsFalseWhenNotExists() {
        // Arrange
        CountryId countryId = new CountryId("PT");
        CityId cityId = new CityId("Lisboa", countryId);

        when(springDataRepo.existsById(cityId.toString())).thenReturn(false);

        // Act
        boolean result = jpaCityRepo.containsOfIdentity(cityId);

        // Assert
        assertFalse(result);
    }
}