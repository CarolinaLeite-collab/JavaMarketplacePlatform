package TOPSECRET.domain.country;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;

/**
 * Aggregate root representing a Country in the domain.
 */
public class Country implements AggregateRoot<CountryId> {

    private final CountryId _countryId;
    private final CountryName _name;

    Country(CountryId id, CountryName name) {
        _countryId = id;
        _name = name;
    }

    public boolean isNamed(CountryName name) {
        return _name.equals(name);
    }

    public boolean isNamed(String name) {
        if (name == null) return false;
        return isNamed(new CountryName(name));
    }

    public boolean isOneOf(CountryName... names) {
        for (CountryName n : names) {
            if (_name.equals(n)) return true;
        }
        return false;
    }

    public CountryId identity() {
        return _countryId;
    }

    public boolean sameAs(Object object) {
        if (this == object) return true;
        if (!(object instanceof Country other)) return false;
        return _countryId.equals(other._countryId);
    }

    public CountryName name() {
        return _name;
    }

    public String getCountryName() {
        return _name.toString();
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
