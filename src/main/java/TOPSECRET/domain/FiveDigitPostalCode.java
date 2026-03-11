package TOPSECRET.domain;

public class FiveDigitPostalCode implements PostalCode {
    private final String _postalCode;
    private final Country _country;

    FiveDigitPostalCode(Country country, String postalCode) {
        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }
        String normalized = normalizePostalCode(postalCode);

        if (!isValidFiveDigitPostalCode(normalized)) {
            throw new IllegalArgumentException("Postal code must contain exactly 5 digits");
        }
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

    private boolean isValidFiveDigitPostalCode(String normalizedPostalCode) {
        return normalizedPostalCode.matches("\\d{5}");
    }

    private String normalizePostalCode(String postalCode) {
        if (postalCode == null) {
            throw new IllegalArgumentException("Postal code cannot be null");
        }
        // remove everything except digits
        return postalCode.replaceAll("\\D", "");
    }
}