package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.persistence.jpa.datamodel.CountryDM;

import java.util.ArrayList;
import java.util.List;

public class CountryAssembler {

    private CountryFactory _countryFactory = new CountryFactory();

    public CountryDM domain2dm(Country country) {
        return new CountryDM(
                country.identity().toString(),
                country.name().toString());
    }

    public Country dm2domain(CountryDM countryDM) {
        return _countryFactory.createCountry(countryDM.getCountryName());
    }

    public List<CountryDM> domainList2dmList(List<Country> countries) {
        List<CountryDM> list = new ArrayList<>();

        for (Country country : countries) {
            list.add(domain2dm(country));
        }

        return list;
    }

    public List<Country> dmList2DomainList(List<CountryDM> countryDMs) {
        List<Country> list = new ArrayList<>();

        for (CountryDM countryDM : countryDMs) {
            list.add(dm2domain(countryDM));
        }

        return list;
    }
}
