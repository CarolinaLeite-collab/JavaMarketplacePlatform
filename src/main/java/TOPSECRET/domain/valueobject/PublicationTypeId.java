package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

/**
 * Represents the unique identifier of a {@link TOPSECRET.domain.publicationtype.PublicationType}.
 * <p>
 * A {@code PublicationTypeId} wraps a {@link String} value derived from the publication type name,
 * normalized to uppercase and trimmed. This ensures that "Book ", " book" and "BOOK"
 * all resolve to the same identifier.
 * </p>
 *
 * <p><b>Validation:</b> The identifier cannot be null, blank, or empty.</p>
 *
 * <p><b>Equality:</b> Two {@code PublicationTypeId} instances are equal if they
 * wrap the same normalized {@link String} value.</p>
 */

public class PublicationTypeId implements DomainId {

    private final String _id;

    public PublicationTypeId(String publicationTypeName) {
        if (publicationTypeName == null || publicationTypeName.isBlank()) {
            throw new IllegalArgumentException("PublicationTypeId cannot be null, blank, or empty.");
        }
        _id = publicationTypeName.trim().replaceAll("\\s+", " ").toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationTypeId)) return false;
        PublicationTypeId pubTypeId = (PublicationTypeId) o;
        return _id.equals(pubTypeId._id);
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
