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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return _countryName.equals(country._countryName);
    }

}
