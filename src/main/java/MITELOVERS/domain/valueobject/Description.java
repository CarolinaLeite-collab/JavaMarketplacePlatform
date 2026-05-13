package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

/**
 * Represents a textual description with a maximum allowed length.
 * <p>
 * Ensures that descriptions are not null, empty, or exceed {@link #MAX_LENGTH} characters.
 * Provides methods to retrieve, update, and get the length of the description.
 * </p>
 */

public class Description implements ValueObject {

    public static final int MAX_LENGTH = 500;

    private final String _description;

    public Description(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null!");
        }

        String normalizedDescription = description.trim();

        if (normalizedDescription.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty or blank!");
        }

        if (normalizedDescription.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Description too long (maximum of " + MAX_LENGTH + " characters)");
        }
        _description = normalizedDescription;
    }

    public int getLength() {
        return _description.length();
    }

    public String getDetailedDescription() {
        return _description + " (" + getLength() + "/" + MAX_LENGTH + ")";
    }

    @Override
    public String toString() {
        return _description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Description other)) return false;
        return _description.equals(other._description);
    }

    @Override
    public int hashCode() {
        return _description.hashCode();
    }

}
