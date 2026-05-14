package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;

/**
 * Represents the unique identifier of a {@link MITELOVERS.domain.publishingcompany.PublishingCompany}.
 * <p>
 * A {@code PublishingCompanyId} wraps a {@link String} value derived from the publishing company name,
 * normalised to uppercase and trimmed. This ensures that "Porto  Editora ", " porto editora" and "PORTO EDITORA"
 * all resolve to the same identifier.
 * </p>
 *
 * <p><b>Validation:</b> The identifier cannot be null or blank.</p>
 *
 * <p><b>Equality:</b> Two {@code PublishingCompanyId} instances are equal if they
 * wrap the same normalized {@link String} value.</p>
 */

public class PublishingCompanyId implements DomainId {

    private final String _id;

    public PublishingCompanyId(String publishingCompanyName) {

        if (publishingCompanyName == null || publishingCompanyName.isBlank()) {
            throw new IllegalArgumentException(
                    "PublishingCompanyId cannot be null, blank, or empty");
        }

        _id = publishingCompanyName.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublishingCompanyId)) return false;

        PublishingCompanyId pubCoId = (PublishingCompanyId) o;

        return _id.equals(pubCoId._id);
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
