package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.persistence.jpa.assembler.CityAssembler;
import MITELOVERS.persistence.jpa.datamodel.CityDataModel;
import MITELOVERS.persistence.springdata.ICitySpringDataRepo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JpaCityRepoTest {

    @Test
    void testJpaCityRepoConstructor() {
        //Arrange
        ICitySpringDataRepo springDataRepo = mock(ICitySpringDataRepo.class);
        CityAssembler cityAssembler = mock(CityAssembler.class);

        //Act + SUT
        new JpaCityRepo(springDataRepo, cityAssembler);
    }

    @Test
    void testSaveResultSavedCity() {
        //Arrange
        ICitySpringDataRepo springDataRepo = mock(ICitySpringDataRepo.class);
        CityAssembler assembler = mock(CityAssembler.class);

        City cityDouble = mock(City.class);

        CityDataModel cityDMDouble = mock(CityDataModel.class);
        CityDataModel savedCityDMDouble = mock(CityDataModel.class);

        when(assembler.domain2DM(cityDouble)).thenReturn(cityDMDouble);
        when(springDataRepo.save(cityDMDouble)).thenReturn(savedCityDMDouble);
        when(assembler.DM2domain(savedCityDMDouble)).thenReturn(cityDouble);

        //SUT
        JpaCityRepo jpaCityRepo = new JpaCityRepo(springDataRepo, assembler);

        //Act
        City result = jpaCityRepo.save(cityDouble);

        //Assert
        assertEquals(cityDouble, result);
    }

    @Test
    void testFindAllReturnsCities() {
        // Arrange
        ICitySpringDataRepo springDataRepo = mock(ICitySpringDataRepo.class);
        CityAssembler assembler = mock(CityAssembler.class);

        CityDataModel cityDM1 = mock(CityDataModel.class);
        CityDataModel cityDM2 = mock(CityDataModel.class);

        List<CityDataModel> dmList = List.of(cityDM1, cityDM2);

        City city1 = mock(City.class);
        City city2 = mock(City.class);

        List<City> cityList = List.of(city1, city2);

        when(springDataRepo.findAll()).thenReturn(dmList);
        when(assembler.DMList2DomainList(dmList)).thenReturn(cityList);

        // SUT
        JpaCityRepo jpaCityRepo = new JpaCityRepo(springDataRepo, assembler);

        // Act
        Iterable<City> result = jpaCityRepo.findAll();

        // Assert
        assertEquals(cityList, result);
    }

    @Test
    void testFindAllKeysReturnsCityIds() {
        // Arrange
        ICitySpringDataRepo springDataRepo = mock(ICitySpringDataRepo.class);
        CityAssembler assembler = mock(CityAssembler.class);

        CityDataModel cityDM = mock(CityDataModel.class);

        when(cityDM.get_name()).thenReturn("Lisboa");
        when(cityDM.get_countryId()).thenReturn("PT");

        when(springDataRepo.findAll()).thenReturn(List.of(cityDM));

        // SUT
        JpaCityRepo jpaCityRepo = new JpaCityRepo(springDataRepo, assembler);

        // Act
        List<CityId> result = jpaCityRepo.findAllKeys();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void testOfIdentityReturnsCityWhenFound() {
        // Arrange
        ICitySpringDataRepo springDataRepo = mock(ICitySpringDataRepo.class);
        CityAssembler assembler = mock(CityAssembler.class);

        CountryId countryId = new CountryId("PT");
        CityId cityId = new CityId("Lisboa", countryId);

        CityDataModel cityDM = mock(CityDataModel.class);
        City city = mock(City.class);

        when(springDataRepo.findById(cityId.toString())).thenReturn(Optional.of(cityDM));
        when(assembler.DM2domain(cityDM)).thenReturn(city);

        // SUT
        JpaCityRepo jpaCityRepo = new JpaCityRepo(springDataRepo, assembler);

        // Act
        Optional<City> result = jpaCityRepo.ofIdentity(cityId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(city, result.get());
    }

    @Test
    void testOfIdentityReturnsEmptyWhenNotFound() {
        // Arrange
        ICitySpringDataRepo springDataRepo = mock(ICitySpringDataRepo.class);
        CityAssembler assembler = mock(CityAssembler.class);

        CountryId countryId = new CountryId("PT");
        CityId cityId = new CityId("Lisboa", countryId);

        when(springDataRepo.findById(cityId.toString())).thenReturn(Optional.empty());

        // SUT
        JpaCityRepo jpaCityRepo = new JpaCityRepo(springDataRepo, assembler);

        // Act
        Optional<City> result = jpaCityRepo.ofIdentity(cityId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testContainsOfIdentityReturnsTrueWhenExists() {
        // Arrange
        ICitySpringDataRepo springDataRepo = mock(ICitySpringDataRepo.class);
        CityAssembler assembler = mock(CityAssembler.class);

        CountryId countryId = new CountryId("PT");
        CityId cityId = new CityId("Lisboa", countryId);

        when(springDataRepo.existsById(cityId.toString())).thenReturn(true);

        // SUT
        JpaCityRepo jpaCityRepo = new JpaCityRepo(springDataRepo, assembler);

        // Act
        boolean result = jpaCityRepo.containsOfIdentity(cityId);

        // Assert
        assertTrue(result);
    }

    @Test
    void testContainsOfIdentityReturnsFalseWhenNotExists() {
        // Arrange
        ICitySpringDataRepo springDataRepo = mock(ICitySpringDataRepo.class);
        CityAssembler assembler = mock(CityAssembler.class);

        CountryId countryId = new CountryId("PT");
        CityId cityId = new CityId("Lisboa", countryId);

        when(springDataRepo.existsById(cityId.toString())).thenReturn(false);

        // SUT
        JpaCityRepo jpaCityRepo = new JpaCityRepo(springDataRepo, assembler);

        // Act
        boolean result = jpaCityRepo.containsOfIdentity(cityId);

        // Assert
        assertFalse(result);
    }

}
