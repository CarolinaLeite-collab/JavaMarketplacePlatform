package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

import java.util.Locale;
import java.util.Objects;

public final class CountryName implements ValueObject {

    private final String _value;

    public CountryName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Country name cannot be null");
        }

        // Normalize spaces: removes leading/trailing and converts double spaces to single
        String normalized = name.trim().replaceAll("\\s+", " ");

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be empty");
        }

        String pattern = "^[\\p{L}]+(?:[ '-][\\p{L}]+)*$";

        if (!normalized.matches(pattern)) {
            throw new IllegalArgumentException("Invalid country name: " + name);
        }

        _value = normalized.toUpperCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountryName other)) return false;
        return Objects.equals(_value, other._value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_value);
    }

    @Override
    public String toString() {
        return _value;
    }
}