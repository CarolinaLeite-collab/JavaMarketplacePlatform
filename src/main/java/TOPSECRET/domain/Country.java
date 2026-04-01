package TOPSECRET.domain;

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
        this._countryId = id;
        this._name = name;
    }

    // Backwards-compatible constructor used by legacy tests
    public Country(String countryName) {
        CountryName name = new CountryName(countryName);
        CountryId id = new CountryId(name.value().substring(0, Math.min(2, name.value().length())));
        this._countryId = id;
        this._name = name;
    }

    public boolean isNamed(CountryName name) {
        return _name.equals(name);
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
        // Domain equality by identity only
        return _countryId.equals(other._countryId);
    }

    public CountryName name() {
        return _name;
    }

    // Backwards compatible accessor for existing code/tests
    public String getCountryName() {
        return _name.toString();
    }

    // Legacy helpers accepting String inputs for compatibility with existing tests
    public boolean isNamed(String name) {
        if (name == null) return false;
        return isNamed(new CountryName(name));
    }

    public boolean isOneOf(String... names) {
        if (names == null) return false;
        CountryName[] arr = new CountryName[names.length];
        for (int i = 0; i < names.length; i++) arr[i] = new CountryName(names[i]);
        return isOneOf(arr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return _name.equals(country._name);
    }

    @Override
    public int hashCode() {
        return _name.hashCode();
    }

}
