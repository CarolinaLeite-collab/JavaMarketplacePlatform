package TOPSECRET.domain.city;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.valueobject.CityId;
import TOPSECRET.domain.valueobject.CountryId;

import java.util.Objects;

/**
 * An {@code City} represents a geographical and administrative unit within a {@link Country}.
 * <p>
 * It is modeled as an {@link AggregateRoot} and is uniquely identified by a {@link CityId}.
 */

public class City implements AggregateRoot<CityId> {

    private final String _name;
    private final CountryId _countryId;
    private final CityId _cityId;

    City(String name, CountryId countryId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        _countryId = Objects.requireNonNull(countryId, "CountryId cannot be null");
        _name = name.trim().replaceAll("\\s+", " ");
        _cityId = new CityId(_name, _countryId);
    }

    public String getName() {
        return _name;
    }

    public CountryId getCountryId() {
        return _countryId;
    }

    @Override
    public CityId identity() {
        return _cityId;
    }

    //Field-base equality
    @Override
    public boolean sameAs(Object object) {
        if (!(object instanceof City other)) return false;
        return _cityId.equals(other._cityId) &&
                _countryId.equals(other._countryId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City other)) return false;
        return _cityId.equals(other._cityId);
    }

    @Override
    public int hashCode() {
        return _cityId.hashCode();
    }

    @Override
    public String toString() {
        return _name + ", " + _countryId.toString();
    }
}