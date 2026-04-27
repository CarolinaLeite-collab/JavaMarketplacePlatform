package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.persistence.jpa.datamodel.CountryDataModel;

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
}
