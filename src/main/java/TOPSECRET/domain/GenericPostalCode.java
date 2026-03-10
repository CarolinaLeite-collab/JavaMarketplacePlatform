package TOPSECRET.domain;

/**
 * Generic implementation of {@link PostalCode} used when no
 * country-specific postal code rules are available.
 * <p>
 * This class performs only basic validation and stores the
 * postal code value as provided.
 */

public class GenericPostalCode implements PostalCode {
    private final Country _country;
    private final String _postalCode;

    public GenericPostalCode(Country country, String postalCode) {
        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("Postal code cannot be null or blank");
        }
        String normalized = normalizePostalCode(postalCode);
        this._postalCode = normalized;
        this._country = country;
    }

    @Override
    public Country getPostalCodeCountry() {
        return _country;
    }

    @Override
    public String getValue() {
        return _postalCode;
    }

    private String normalizePostalCode(String postalCode) {
        return postalCode.trim();
    }
}
