package TOPSECRET.domain;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents an immutable, validated email address. This class ensures that only
 * properly formatted emails (with a local part, '@', and domain) can be created,
 * normalizes them to lowercase, and implements correct equals and hashCode behavior
 * for safe use in collections.
 */

public final class Email {

    // Establishing a regex to guarantee emails in the format: local@domain with subdomains
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9-.]+(\\.[A-Za-z0-9-]+)+$");

    // Email should never change after creation
    private final String _email;

    public Email(String email) {

        // Throws exception if email is null or blank
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank!");
        }

        // Trim extra blank spaces from beginning or end before validating format
        String trimmed = email.trim();

        // Throws exception if email has invalid format
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Invalid email format!");
        }

        // Emails are case-insensitive, normalize to lowercase
        _email = trimmed.toLowerCase();
    }

    public String getValue() {
        return _email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return _email.equals(email._email);
    }

    // Normalizing hash codes will prevent false negatives
    @Override
    public int hashCode() {
        return Objects.hash(_email);
    }

    @Override
    public String toString() {
        return _email;
    }

}
