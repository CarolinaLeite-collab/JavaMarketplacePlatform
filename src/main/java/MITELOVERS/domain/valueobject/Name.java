package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

/**
 * Represents a validated personal or entity name.
 * <p>
 * Ensures that the name is not null, not blank, between 2 and 80 characters,
 * and contains only letters, spaces, hyphens, or apostrophes. Provides normalization
 * by trimming and collapsing multiple spaces.
 * </p>
 */

public class Name implements ValueObject {

    private final String _name;

    public Name(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        String normalized = normalize(name);

        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }

        if (normalized.length() < 2 || normalized.length() > 80) {
            throw new IllegalArgumentException("Name must have between 2 and 80 characters");
        }

        if (!normalized.matches("[\\p{L}]+([\\p{L}\\s'-]*[\\p{L}])?")) {
            throw new IllegalArgumentException("Name contains invalid characters");
        }
        _name = normalized;
    }

    public String getName() {
            return _name;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name other)) return false;
        return _name.equals(other._name);
    }

    @Override
    public int hashCode() {
            return _name.hashCode();
        }

    @Override
    public String toString() {
            return _name;
        }

    private static String normalize(String raw) {

        return raw.trim().replaceAll("\\s+", " ");
    }
}
