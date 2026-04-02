package TOPSECRET.domain;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.valueobject.CityId;
import TOPSECRET.domain.valueobject.CountryId;

import java.util.Objects;

public class City implements AggregateRoot<CityId> {

    private final String _name;
    private final Country _country;
    private final CityId _cityId;

    City(String name, Country country) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        _country = Objects.requireNonNull(country, "Country cannot be null");
        _name = name.trim().replaceAll("\\s+", " ");
        _cityId = new CityId(_name, country.identity());
    }

    public String getName() {
        return _name;
    }

    public Country getCountry() {
        return _country;
    }

    @Override
    public CityId identity() {
        return _cityId;
    }

    @Override
    public boolean sameAs(Object object) {
        if (!(object instanceof City other)) return false;
        return _cityId.equals(other._cityId);
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
        return _name + ", " + _country.getCountryName();
    }
}