package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

/**
 * Country ISO code identifier (ISO 3166-1 alpha-2)
 */
public final class CountryId implements DomainId {

    private final String _code;

    public CountryId(String code) {
        if (code == null) throw new IllegalArgumentException("CountryId cannot be null");
        String normalized = code.trim().toUpperCase();
        if (!normalized.matches("^[A-Z]{2}$"))
            throw new IllegalArgumentException("CountryId must be exactly 2 uppercase letters (ISO 3166-1 alpha-2)");
        this._code = normalized;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CountryId other) {
            return this == o || _code.equals(other._code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return _code.hashCode();
    }

    @Override
    public String toString() {
        return _code;
    }
}

