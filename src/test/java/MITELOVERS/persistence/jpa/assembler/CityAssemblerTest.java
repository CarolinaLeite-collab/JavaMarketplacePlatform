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

        CityDataModel result = assembler.domain2DM(city);

        assertEquals(city.getName().toString(), result.get_name());
        assertEquals(city.getCountryId().toString(), result.get_countryId());
        assertEquals(city.identity().toString(), result.get_cityId());
    }

    @Test
    void shouldConvertDataModelToDomain() {
        CityFactory factory = new CityFactory();
        CityAssembler assembler = new CityAssembler(factory);
        CityDataModel cityDM = new CityDataModel("PTporto, PT", "Porto", "PT");

        City result = assembler.DM2domain(cityDM);

        assertEquals(cityDM.get_name().toString(), result.getName());
        assertEquals(cityDM.get_cityId().toString(), result.identity().toString());
        assertEquals(cityDM.get_countryId().toString(), result.getCountryId().toString());

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

        List<City> cityList = assembler.DMList2DomainList(cityDMs);

        assertEquals(cityDMs.size(), cityList.size());
        assertEquals(cityDMs.get(0).get_name().toString(), cityDMs.get(0).get_name());
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

        List<CityDataModel> cityDMs = assembler.domainList2DMList(cities);

        assertEquals(cityDMs.size(), cityDMs.size());
        assertEquals(cityDMs.get(0).get_name().toString(), cityDMs.get(0).get_name());
    }
}