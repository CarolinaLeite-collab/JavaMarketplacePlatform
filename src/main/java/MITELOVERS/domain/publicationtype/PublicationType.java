package MITELOVERS.domain.publicationtype;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.PublicationTypeId;

/**
 * Represents the type of publication (e.g. BOOK, MAGAZINE), used to
 * classify and organize publications.
 * <p>
 * A {@code PublicationType} is an aggregate root identified by a
 * {@link MITELOVERS.domain.valueobject.PublicationTypeId}. The identity is
 * derived from the publication type name, which is validated (non-null,
 * non-blank, and non-empty) and normalized by trimming whitespace and
 * converting it to uppercase.
 * </p>
 */

public class PublicationType implements AggregateRoot<PublicationTypeId> {

    private final PublicationTypeId _id;

    PublicationType(String publicationTypeName) {

        _id = new PublicationTypeId(publicationTypeName);

    }

    PublicationType(PublicationTypeId id) {

        _id = id;

    }

    @Override
    public PublicationTypeId identity() {
        return _id;
    }

    @Override
    public boolean sameAs(Object object) {
        return equals(object);
    }

    @Override
    public String toString() {
        return _id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationType)) return false;
        PublicationType pubType = (PublicationType) o;
        return _id.equals(pubType._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
