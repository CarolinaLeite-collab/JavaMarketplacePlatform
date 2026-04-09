package TOPSECRET.domain.country;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.CountryId;
import TOPSECRET.domain.valueobject.CountryName;

public class Country implements AggregateRoot<CountryId> {

    private final CountryId _countryId;
    private final CountryName _name;

    // Package-private constructor: Encourages use of the Factory
    Country(String countryName) {
        this._name = new CountryName(countryName);
        this._countryId = new CountryId(this._name);
    }

    public CountryId identity() {
        return _countryId;
    }

    public CountryName name() {
        return _name;
    }

    /**
     * Backward-compatible accessor kept for existing callers.
     */
    public String getCountryName() {
        return _name.toString();
    }

    @Override
    public boolean sameAs(Object object) {
        if (this == object) return true;
        if (!(object instanceof Country other)) return false;
        return _countryId.equals(other._countryId);
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