package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;
import TOPSECRET.domain.PhonePrefix;

/**
 * Phone number composed of a {@link PhonePrefix} and a national number.
 * The national number is normalized to digits only (4–12 digits).
 * E.164-like representation: +{prefix}{nationalNumber}.
 */
public class Phone implements ValueObject {

    private final PhonePrefix _prefix;
    private final String _nationalNumber;

    public Phone(PhonePrefix prefix, String nationalNumber) {

        if (prefix == null) {
            throw new IllegalArgumentException("Phone prefix cannot be null");
        }
        if (nationalNumber == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }

        String cleaned = nationalNumber
                .replaceAll("\\s", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");

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

    public String getE164() {
        return _prefix.getValue() + _nationalNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phone phone)) return false;
        String normalizedThis = _prefix.getValue().replaceAll("\\D", "") + _nationalNumber.replaceAll("\\s+", "");
        String normalizedOther = phone._prefix.getValue().replaceAll("\\D", "") + phone._nationalNumber.replaceAll("\\s+", "");

        return normalizedThis.equals(normalizedOther);
    }

    @Override
    public int hashCode() {
        String normalized = _prefix.getValue().replaceAll("\\D", "") + _nationalNumber.replaceAll("\\s+", "");
        return normalized.hashCode();
    }

    @Override
    public String toString() {
        return getE164();
    }
}