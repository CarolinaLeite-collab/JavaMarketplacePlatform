package TOPSECRET.domain;

import java.util.Locale;

/**
 * Represents a country identified by its validated and normalized name.
 * <p>
 * The name cannot be null, empty, or contain invalid characters.
 * Comparison methods normalize input before matching.
 * </p>
 */

public class Country {
    private final String _countryName;

    Country(String countryName) {
            _countryName = sanitizedCountryName(countryName);
    }

    public boolean isNamed(String name) {
        return _countryName.equals(sanitizedCountryName(name));
    }

    public boolean isOneOf(String... names) {
        for (String name : names) {
            if (_countryName.equals(sanitizedCountryName(name))) {
                return true;
            }
        }
        return false;
    }

    public String getCountryName() {
        return _countryName;
    }

    private String sanitizedCountryName(String countryName) {
        if (countryName == null) {
            throw new IllegalArgumentException("Country name cannot be null");
        }
        String result = countryName.trim();
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be empty");
        }

        String pattern = "^[\\p{L}]+(?: [\\p{L}]+)*$";
        if (!result.matches(pattern)) {
            throw new IllegalArgumentException("Invalid country name: " + countryName);
        }

        result = result.replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return _countryName.equals(country._countryName);
    }

}
