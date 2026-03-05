package TOPSECRET.domain;

import java.util.Locale;

/**
 * Represents a country in the system, associated with an administrator.
 * <p>
 * Each Country has a name and a User who registers it.
 * Equality between Country instances is determined solely by the country name.
 */
public class Country {
    private final String _countryName;

    public Country(String countryName) {
        _countryName = sanitizedCountryName(countryName);
    }

    // Added getter to allow other components to locate countries by name
    public String getCountryName() {
        return _countryName;
    }

    public String sanitizedCountryName(String countryName) {
        if (countryName == null) {
            throw new IllegalArgumentException("Country name cannot be null");
        }

        String result = countryName.trim();

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be empty");
        }

        // Only allows Unicode letters and spaces
        // must start and end with a letter and words may only be separated by a single space
        String pattern = "^[\\p{L}]+(?: [\\p{L}]+)*$";
        if(!result.matches(pattern)) {
            throw new IllegalArgumentException("Invalid country name: " + countryName);
        }

        //eliminates multiple spaces and converts to uppercase
        result = result.replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return _countryName.equalsIgnoreCase(country._countryName);
    }

}
