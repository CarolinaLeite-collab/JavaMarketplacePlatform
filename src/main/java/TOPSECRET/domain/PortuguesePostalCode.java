package TOPSECRET.domain;

public class PortuguesePostalCode implements PostalCode {
    private final String _postalCode;
    private final Country _country;

    PortuguesePostalCode(Country country, String postalCode) {
        if (!isCountryPortugal(country)){
            throw new IllegalArgumentException("Postal code must belong to Portugal");
        }
        if (!isValidPortuguesePostalCode(postalCode)) {
            throw new IllegalArgumentException("Invalid Portuguese postal code");
        }
        this._postalCode = postalCode.trim();
        this._country = country;
    }

    @Override
    public Country getPostalCodeCountry() {
        return _country;
    }

    private boolean isValidPortuguesePostalCode(String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) return false;

        return postalCode.trim().matches("[1-9]\\d{3}-\\d{3}");
    }

    private boolean isCountryPortugal(Country country){
        return country != null && "PORTUGAL".equals(country.getCountryName());
    }
}
