package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;
import java.util.Objects;

public class CityId implements DomainId {

    private final String _normalizedName;
    private final CountryId _countryId;

    public CityId(String cityName, CountryId countryId) {
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (countryId == null) {
            throw new IllegalArgumentException("CountryId cannot be null");
        }
        _normalizedName = cityName.trim().toLowerCase();
        _countryId = countryId;
    }

    public String getNormalizedName() {
        return _normalizedName;
    }

    public CountryId getCountryId() {
        return _countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CityId other)) return false;
        return _normalizedName.equals(other._normalizedName)
                && _countryId.equals(other._countryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_normalizedName, _countryId);
    }

    @Override
    public String toString() {
        return _normalizedName + ", " + _countryId.toString();
    }
}