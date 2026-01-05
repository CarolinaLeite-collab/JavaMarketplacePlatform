package TOPSECRET.domain;

import java.util.Objects;

/**
 * Phone country/area prefix normalized to leading '+' and 1–3 digits.
 */
public class PhonePrefix {

    private final String _value; // normalized with leading '+'

    public PhonePrefix(String rawPrefix) {
        if (rawPrefix == null) {
            throw new IllegalArgumentException("Phone prefix cannot be null");
        }
        String trimmed = rawPrefix.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Phone prefix cannot be blank");
        }

        String normalized = trimmed.replace(" ", "");
        if (normalized.startsWith("+")) {
            normalized = normalized.substring(1);
        }

        if (!normalized.matches("\\d{1,3}")) {
            throw new IllegalArgumentException("Phone prefix must have 1 to 3 digits");
        }

        this._value = "+" + normalized;
    }

    public String getValue() {
        return _value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhonePrefix)) return false;
        PhonePrefix that = (PhonePrefix) o;
        return _value.equals(that._value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_value);
    }

    @Override
    public String toString() {
        return _value;
    }
}