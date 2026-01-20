package TOPSECRET.domain;

import java.util.Objects;

public class City {
    private final String _name;
    private final Country _country;

    public City(String name, Country country) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }
        _name = name.trim();
        _country = country;
    }

    public String getName() {
        return _name;
    }

    public Country getCountry() {
        return _country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City city)) return false;
        return _name.equals(city._name) && _country.equals(city._country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _country);
    }

    @Override
    public String toString() {
        return String.format("%s, %s", _name, _country != null ? _country.toString() : "");
    }
}
