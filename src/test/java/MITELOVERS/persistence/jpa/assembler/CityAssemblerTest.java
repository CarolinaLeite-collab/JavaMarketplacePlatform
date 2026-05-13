package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.city.CityFactory;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.persistence.jpa.datamodel.CityDataModel;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityAssemblerTest {

    @Test
    void shouldConvertDomainToDataModel() {
        CityFactory factory = new CityFactory();
        CityAssembler assembler = new CityAssembler(factory);
        CountryId countryId = new CountryId("PT");
        City city = factory.createCity("Porto", countryId);

        CityDataModel result = assembler.toDataModel(city);

        assertEquals(city.getName().toString(), result.getName());
        assertEquals(city.getCountryId().toString(), result.getCountryId());
        assertEquals(city.identity().toString(), result.getCityId());
    }

    @Test
    void shouldConvertDataModelToDomain() {
        CityFactory factory = new CityFactory();
        CityAssembler assembler = new CityAssembler(factory);
        CityDataModel cityDM = new CityDataModel("PTporto", "Porto", "PT");

        City result = assembler.toDomain(cityDM);

        assertEquals(cityDM.getName().toString(), result.getName());
        assertEquals(cityDM.getCityId().toString(), result.identity().toString());
        assertEquals(cityDM.getCountryId().toString(), result.getCountryId().toString());

    }

    @Test
    void shouldConvertDataModelListToDomain() {
        CityFactory factory = new CityFactory();
        CityAssembler assembler = new CityAssembler(factory);
        List<CityDataModel> cityDMs = new ArrayList<>();
        CityDataModel cityDM1 = new CityDataModel("PTporto, PT", "Porto", "PT");
        CityDataModel cityDM2 = new CityDataModel("PTporto, PT", "Porto", "PT");
        cityDMs.add(cityDM1);
        cityDMs.add(cityDM2);

        List<City> cityList = assembler.toDomainList(cityDMs);

        assertEquals(cityDMs.size(), cityList.size());
        assertEquals(cityDMs.get(0).getName().toString(), cityDMs.get(0).getName());
    }

    @Test
    void shouldConvertDomainlListToDataModel() {
        CityFactory factory = new CityFactory();
        CityAssembler assembler = new CityAssembler(factory);
        List<City> cities = new ArrayList<>();
        CountryId countryId1 = new CountryId("PT");
        CountryId countryId2 = new CountryId("PT");
        City city1 = factory.createCity("Porto1", countryId1);
        City city2 = factory.createCity("Porto2", countryId2);
        cities.add(city1);
        cities.add(city2);

        List<CityDataModel> cityDMs = assembler.toDataModelList(cities);

        assertEquals(cityDMs.size(), cityDMs.size());
        assertEquals(cityDMs.get(0).getName().toString(), cityDMs.get(0).getName());
    }
}