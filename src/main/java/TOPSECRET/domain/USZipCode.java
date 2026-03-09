package TOPSECRET.domain;

public class USZipCode implements PostalCode {
    private final String _postalCode;
    private final Country _country;

    USZipCode(Country country, String postalCode) {
        if (!isCountryUnitedStates(country)) {
            throw new IllegalArgumentException("Postal code must belong to the United States");
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

    private boolean isValidUSZipCode(String normalizedPostalCode) {
        return normalizedPostalCode.matches("\\d{5}") ||
                normalizedPostalCode.matches("\\d{5}-\\d{4}");
    }

    private boolean isCountryUnitedStates(Country country) {
        if (country == null) return false;
        String name = country.getCountryName();
        return "UNITED STATES".equals(name) ||
                "US".equals(name) ||
                "USA".equals(name);
    }

    private String normalizePostalCode(String postalCode) {
        if (postalCode == null) {
            throw new IllegalArgumentException("Postal code cannot be null");
        }
        // remove everything except digits
        String digits = postalCode.replaceAll("\\D", "");
        if (digits.length() == 5) {
            return digits;
        }
        if (digits.length() == 9) {
            return digits.substring(0, 5) + "-" + digits.substring(5);
        }
        throw new IllegalArgumentException("US ZIP code must have 5 or 9 digits");
    }
}


