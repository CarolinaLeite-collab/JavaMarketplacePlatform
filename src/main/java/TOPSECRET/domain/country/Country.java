package TOPSECRET.domain.country;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;

public class Country implements AggregateRoot<CountryId> {

    private final CountryId _countryId;
    private final CountryName _name;

    Country(String countryName) {
        _name = new CountryName(countryName);
        _countryId = new CountryId(_name);
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
    public boolean sameAs(Object other) {
        if (!(other instanceof Country)) {
            return false;
        }
        final Country that = (Country) other;
        if (this == that) {
            return true;
        }
        return identity().equals(that.identity());
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