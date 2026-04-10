package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;
import java.util.Locale;
import java.util.Objects;

/**
 * Country identifier that generates an ISO 3166-1 alpha-2 code
 * based on a validated CountryName.
 */
public final class CountryId implements DomainId {

    private final String _code;

    // Backward-compatible constructor that accepts an ISO 3166-1 alpha-2 code.
    public CountryId(String isoCode) {
        _code = normalizeAndValidateIsoCode(isoCode);
    }

    public CountryId(CountryName name) {
        // Technical safety: ensures we don't call .toString() on null
        Objects.requireNonNull(name, "CountryName is required to generate an ID");

        _code = generateIsoCode(name.toString());
    }

    private String normalizeAndValidateIsoCode(String isoCode) {
        if (isoCode == null || isoCode.isBlank()) {
            throw new IllegalArgumentException("Country ISO code cannot be null or blank");
        }

        String normalized = isoCode.trim().toUpperCase(Locale.ROOT);
        for (String validIso : Locale.getISOCountries()) {
            if (validIso.equals(normalized)) {
                return normalized;
            }
        }

        throw new IllegalArgumentException("Invalid ISO 3166 code: " + isoCode);
    }

    private String generateIsoCode(String name) {
        for (String isoCode : Locale.getISOCountries()) {
            Locale l = new Locale("", isoCode);
            // Matches against the English display name (e.g., "PORTUGAL" vs "Portugal")
            if (l.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(name)) {
                return isoCode.toUpperCase(Locale.ROOT);
            }
        }
        throw new IllegalArgumentException("No ISO 3166 code found for: " + name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountryId other)) return false;
        return Objects.equals(_code, other._code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_code);
    }

    @Override
    public String toString() {
        return _code;
    }
}