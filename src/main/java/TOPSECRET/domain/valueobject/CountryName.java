package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

import java.util.Locale;
import java.util.Objects;

/**
 * Value object representing a validated and normalized country name.
 */
public final class CountryName implements ValueObject {

    private final String _value;

    public CountryName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Country name cannot be null");
        }

        // 1. NORMALIZE FIRST: This turns "United    Kingdom" into "United Kingdom"
        String normalized = name.trim().replaceAll("\\s+", " ");

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be empty");
        }

        // 2. VALIDATE SECOND: The regex now sees the cleaned string
        String pattern = "^[\\p{L}]+(?: [\\p{L}]+)*$";
        if (!normalized.matches(pattern)) {
            throw new IllegalArgumentException("Invalid country name: " + name);
        }

        // 3. FINAL FORMATTING
        this._value = normalized.toUpperCase(Locale.ROOT);
    }

    public String value() {
        return _value;
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