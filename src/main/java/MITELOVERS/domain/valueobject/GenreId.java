package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;


/**
 * Represents the unique identifier of a {@link MITELOVERS.domain.genre.Genre}.
 * <p>
 * A {@code GenreId} wraps a {@link String} value derived from the genre name,
 * normalised to uppercase. This ensures that "Fiction", "fiction" and "FICTION"
 * all resolve to the same identifier.
 * </p>
 *
 * <p><b>Validation:</b> The identifier cannot be null or blank.</p>
 *
 * <p><b>Equality:</b> Two {@code GenreId} instances are equal if they
 * wrap the same normalised {@link String} value.</p>
 */

public class GenreId implements DomainId {

    private final String _id;

    public GenreId(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("GenreId cannot be null or blank");
        _id = name.trim().toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenreId other)) return false;
        return _id.equals(other._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    @Override
    public String toString() {
        return _id;
    }
}

