package TOPSECRET.domain;

import java.time.LocalDate;

public class Country {
    private final String _countryName;
    private final User _admin;
    private final LocalDate _createdDate;

    public Country(String countryName, User admin, LocalDate createdDate) {

        if (countryName == null || admin == null || createdDate == null) {
            throw new IllegalArgumentException("Country parameters cannot be null");
        }

        _countryName = countryName;
        _admin = admin;
        _createdDate = createdDate;
    }

    // Added getter to allow other components to locate countries by name
    public String getCountryName() {
        return _countryName;
    }

    // Optional getters for tests/controllers
    public User getAdmin() {
        return _admin;
    }

    public LocalDate getCreatedDate() {
        return _createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return _countryName.equals(country._countryName);
    }

}
