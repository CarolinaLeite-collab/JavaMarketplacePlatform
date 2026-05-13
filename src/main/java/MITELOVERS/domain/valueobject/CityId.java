package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;

import java.util.Objects;

public class CityId implements DomainId {

    private final String _normalizedName;

    public CityId(String cityName, CountryId countryId) {
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (countryId == null) {
            throw new IllegalArgumentException("CountryId cannot be null");
        }
        _normalizedName = countryId.toString() + cityName.trim().toLowerCase();
    }

    public CityId(String cityName){
        _normalizedName = Objects.requireNonNull(cityName, "CityName cannot be null");
    }

    public String getNormalizedName() {
        return _normalizedName;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CityId other)) return false;
        return _normalizedName.equals(other._normalizedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_normalizedName);
    }

    @Override
    public String toString() {
        return _normalizedName;
    }
}
