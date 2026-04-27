package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.persistence.jpa.datamodel.CountryDataModel;

import java.util.ArrayList;
import java.util.List;

public class CountryAssembler {

    private CountryFactory _countryFactory = new CountryFactory();

    public CountryDataModel domain2dm(Country country) {
        return new CountryDataModel(
                country.identity().toString(),
                country.name().toString());
    }

    public Country dm2domain(CountryDataModel countryDataModel) {
        return _countryFactory.createCountry(countryDataModel.getCountryName());
    }

    public List<CountryDataModel> domainList2dmList(List<Country> countries) {
        List<CountryDataModel> list = new ArrayList<>();

        for (Country country : countries) {
            list.add(domain2dm(country));
        }

        return list;
    }

    public List<Country> dmList2DomainList(List<CountryDataModel> countryDataModels) {
        List<Country> list = new ArrayList<>();

        for (CountryDataModel countryDataModel : countryDataModels) {
            list.add(dm2domain(countryDataModel));
        }

        return list;
    }
}
