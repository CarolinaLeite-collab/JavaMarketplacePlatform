package MITELOVERS.domain.country;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.domain.valueobject.CountryName;
/**
 * A {@code Country} represents geographical country identified by its name.
 * <p>
 * It is modeled as an {@link AggregateRoot} and is uniquely identified by a {@link CountryId},
 * which is derived from its {@link CountryName}.
 */

public class Country implements AggregateRoot<CountryId> {

    private final CountryId _countryId;
    private final CountryName _name;

    Country(String countryName) {
        _name = new CountryName(countryName);
        _countryId = new CountryId(_name);
    }

    Country(CountryId countryId, String countryName) {
        _name = new CountryName(countryName);
        _countryId = countryId;
    }

    public CountryId identity() {
        return _countryId;
    }

    public CountryName name() {
        return _name;
    }

    @Deprecated
    public String getCountryName() {
        return _name.toString();
    }

    @Override
     public boolean sameAs(Object object) {
        if (object instanceof Country) {
            Country other = (Country) object;

            if (this._name.equals(other._name)
            )
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country other)) return false;
        return _countryId.equals(other._countryId);
    }

    @Override
    public int hashCode() {
        return _countryId.hashCode();
    }
}
