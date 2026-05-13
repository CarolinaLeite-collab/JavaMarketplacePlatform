package MITELOVERS.domain.publicationtype;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.PublicationTypeId;

/**
 * Aggregate root representing a type of publication (e.g. BOOK, MAGAZINE),
 * identified by a {@link PublicationTypeId} and created via {@link PublicationTypeFactory}.
 * Can be instantiated from a name string or reconstructed from an existing {@link PublicationTypeId}.
 */

public class PublicationType implements AggregateRoot<PublicationTypeId> {

    private final PublicationTypeId _id;

    PublicationType(String publicationTypeName) {

        _id = new PublicationTypeId(new Name(publicationTypeName).toString());

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
