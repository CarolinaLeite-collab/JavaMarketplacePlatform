package TOPSECRET.domain;

import java.util.Objects;

/**
 * Phone number composed of a {@link PhonePrefix} and a national number.
 * The national number is normalized to digits only (4–12 digits).
 */
public class Phone {

    private final PhonePrefix _prefix;
    private final String _nationalNumber;

    public Phone(PhonePrefix prefix, String nationalNumber) {
        if (prefix == null) {
            throw new IllegalArgumentException("Phone prefix cannot be null");
        }
        if (nationalNumber == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }

        String cleaned = nationalNumber.replaceAll("[\\s\\-()]", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be blank");
        }
        if (!cleaned.matches("\\d{4,12}")) {
            throw new IllegalArgumentException("Phone number must have 4 to 12 digits");
        }

        _prefix = prefix;
        _nationalNumber = cleaned;
    }

    public PhonePrefix getPrefix() {
        return _prefix;
    }

    public String getNationalNumber() {
        return _nationalNumber;
    }

    /**
     * E.164-like representation: +<prefix><number>
     */
    public String getE164() {
        return _prefix.getValue() + _nationalNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phone)) return false;
        Phone phone = (Phone) o;
        return _prefix.equals(phone._prefix) && _nationalNumber.equals(phone._nationalNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_prefix, _nationalNumber);
    }

    @Override
    public String toString() {
        return getE164();
    }
}