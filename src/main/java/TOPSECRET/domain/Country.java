package TOPSECRET.domain;

import java.util.Locale;

/**
 * Represents a country registered in the system.
 *
 * <p>
 * A {@code Country} is uniquely identified by its name. The name is validated
 * and normalized during construction.
 * </p>
 *
 * <p><b>Normalization rules:</b></p>
 * <ul>
 *   <li>Leading and trailing whitespace is removed.</li>
 *   <li>Multiple spaces between words are collapsed into a single space.</li>
 *   <li>The resulting name is converted to uppercase using {@link Locale#ROOT}.</li>
 * </ul>
 *
 * <p><b>Validation rules:</b></p>
 * <ul>
 *   <li>The name cannot be {@code null}.</li>
 *   <li>The name cannot be empty.</li>
 *   <li>Only Unicode letters and single spaces between words are allowed.</li>
 * </ul>
 *
 * <p>
 * Instances of {@code Country} are considered equal if their normalized names
 * are equal, using a case-insensitive comparison.
 * </p>
 */

public class Country {
    private final String _countryName;

    Country(String countryName) {
        _countryName = sanitizedCountryName(countryName);
    }

    // Added getter to allow other components to locate countries by name
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
