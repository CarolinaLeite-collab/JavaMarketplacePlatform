package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.city.CityFactory;
import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.persistence.jpa.datamodel.CityDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CityAssembler {

    private CityFactory cityFactory;

    public CityDataModel toDataModel(City city) {
        return new CityDataModel(
                city.identity().toString(),
                city.getName(),
                city.getCountryId().toString());
    }

    public City toDomain(CityDataModel cityDM) {
        return cityFactory.createCity(
                cityDM.get_name(),
                new CountryId(cityDM.get_countryId()),
                new CityId(cityDM.get_name(), new CountryId(cityDM.get_countryId())));
    }

    public List<CityDataModel> toDataModelList(List<City> cities) {
        List<CityDataModel> list = new ArrayList<>();

        for (City city : cities) {
            list.add(toDataModel(city));
        }

        return list;
    }

    public List<City> toDomainList(List<CityDataModel> cityDMs) {
        List<City> list = new ArrayList<>();

        for (CityDataModel cityDM : cityDMs) {
            list.add(toDomain(cityDM));
        }

        return list;
    }
}
