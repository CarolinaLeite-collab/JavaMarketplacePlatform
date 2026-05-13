package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.country.CountryFactory;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.persistence.jpa.datamodel.CountryDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler responsible for converting between {@link Country} domain objects
 * and {@link CountryDataModel} persistence objects.
 */

@Component
@AllArgsConstructor

public class CountryAssembler {

    private final CountryFactory _countryFactory;

    public CountryDataModel toDataModel(Country country) {

        if (country == null)
            throw new IllegalArgumentException("Country cannot be null");

        return new CountryDataModel(
                country.identity().toString(),
                country.name().toString());
    }

    public Country toDomain(CountryDataModel countryDataModel) {

        if (countryDataModel == null)
            throw new IllegalArgumentException("Country cannot be null");

        CountryId countryId = new CountryId(countryDataModel.getCountryId());
        return _countryFactory.createCountry(countryId, countryDataModel.getCountryName());
    }
}
