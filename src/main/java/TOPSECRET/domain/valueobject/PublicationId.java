package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

/**
 * Represents the unique technical identifier of a {@link TOPSECRET.domain.Publication}.
 * <p>
 * A {@code PublicationId} wraps a {@link String} value generated from a UUID,
 * ensuring stable identity across the system regardless of changes to the
 * publication's business attributes.
 * </p>
 *
 * <p><b>Validation:</b> The identifier cannot be null or blank.</p>
 *
 * <p><b>Equality:</b> Two {@code PublicationId} instances are equal if they
 * wrap the same {@link String} value.</p>
 */


public class PublicationId implements DomainId {

    private final String _id;

    public PublicationId(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("PublicationId cannot be null or blank");
        _id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationId other)) return false;
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

