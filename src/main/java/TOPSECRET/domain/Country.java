package TOPSECRET.domain;

/**
 * Represents a country in the system, associated with an administrator.
 * <p>
 * Each Country has a name and a User who registers it.
 * Equality between Country instances is determined solely by the country name.
 */
public class Country {
    private final String _countryName;
    private final User _admin;

    public Country(String countryName, User admin) {

        if (countryName == null || admin == null) {
            throw new IllegalArgumentException("Country parameters cannot be null");
        }

        _countryName = countryName;
        _admin = admin;
    }

    // Added getter to allow other components to locate countries by name
    public String getCountryName() {
        return _countryName;
    }

    // Optional getters for tests/controllers
    public User getAdmin() {
        return _admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return _countryName.equalsIgnoreCase(country._countryName);
    }

}
