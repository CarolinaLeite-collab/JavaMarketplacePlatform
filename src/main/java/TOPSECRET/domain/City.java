package TOPSECRET.domain;

import java.util.Objects;

/**
 * Represents a city within a specific country.
 * <p>
 * Ensures that the city name is not null or blank and that the country is specified.
 * </p>
 */

public class City {
    private final String _name;
    private final String _normalizedName;
    private final Country _country;

    // Creates a new city while normalizing the name and validating the country.
    City(String name, Country country) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }

        String cleaned = name.trim().replaceAll("\\s+", " ");
        _name = cleaned;
        _normalizedName = cleaned.toLowerCase();
        _country = country;
    }

    public String getName() {
        return _name;
    }

    // Returns the country that owns this city.
    public Country getCountry() {
        return _country;
    }

    // Equality is based on case-insensitive name comparison and exact country match.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City city)) return false;
        return _name.equalsIgnoreCase(city._name) && _country.equals(city._country);
    }

    // Hash code follows the same logic as {@link #equals(Object)} to stay consistent.
    @Override
    public int hashCode() {
        return Objects.hash(_normalizedName, _country);
    }

    // String representation is "CityName, CountryName" to keep formatting stable for tests.
    @Override
    public String toString() {
        return _name + ", " + _country.getCountryName();
    }
}
