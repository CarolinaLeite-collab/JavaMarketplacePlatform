package MITELOVERS.domain.city;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;

import java.util.Objects;

/**
 * A {@code City} represents a geographical and administrative unit within a {@link Country}.
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

    City(String cityName, CountryId countryId, CityId cityId) {
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }

        _countryId = Objects.requireNonNull(countryId, "CountryId cannot be null");
        _cityId = Objects.requireNonNull(cityId, "CityId cannot be null");
        _name = cityName.trim().replaceAll("\\s+", " ");
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
        return _name + ", " + _countryId.toString();
    }
}
