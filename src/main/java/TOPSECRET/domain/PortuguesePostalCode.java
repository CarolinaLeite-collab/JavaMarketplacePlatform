package TOPSECRET.domain;

public class PortuguesePostalCode implements PostalCode {
    private final String _postalCode;
    private final Country _country;

    PortuguesePostalCode(Country country, String postalCode) {
        if (!isCountryPortugal(country)){
            throw new IllegalArgumentException("Postal code must belong to Portugal");
        }
        String normalized = normalizePostalCode(postalCode);

        if (!isValidPortuguesePostalCode(normalized)) {
            throw new IllegalArgumentException("Invalid Portuguese postal code");
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

    private boolean isValidPortuguesePostalCode(String normalizedPostalCode) {
        return normalizedPostalCode.matches("[1-9]\\d{3}-\\d{3}");
    }

    private boolean isCountryPortugal(Country country){
        return country != null && "PORTUGAL".equals(country.getCountryName());
    }

    private String normalizePostalCode(String postalCode) {
        if (postalCode == null) {
            throw new IllegalArgumentException("Postal code cannot be null");
        }
        // remove everything except digits
        String digits = postalCode.replaceAll("\\D", "");
        if (digits.length() != 7) {
            throw new IllegalArgumentException("Portuguese postal code has 7 digits");
        }
        // format NNNN-NNN
        return digits.substring(0, 4) + "-" + digits.substring(4);
    }
}
