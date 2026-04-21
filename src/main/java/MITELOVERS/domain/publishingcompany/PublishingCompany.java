package MITELOVERS.domain.publishingcompany;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.PublishingCompanyId;

/**
 * Represents the organization or company that formally releases the work of one edition.
 *
 * <p>
 * A {@code PublishingCompany} is an aggregate root identified by a
 * {@link MITELOVERS.domain.valueobject.PublishingCompanyId}. The identity is
 * derived from the publishing company name, which is validated (non-null,
 * non-blank, and non-empty) and normalized by trimming whitespace and
 * converting it to uppercase.
 * </p>
 */

public class PublishingCompany implements AggregateRoot<PublishingCompanyId> {

    private final PublishingCompanyId _id;

    PublishingCompany(String publishingCompanyName) {
        _id = new PublishingCompanyId(publishingCompanyName);
    }

    @Override
    public PublishingCompanyId identity() {
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
        if (!(o instanceof PublishingCompany)) return false;
        PublishingCompany pubCo = (PublishingCompany) o;
        return _id.equals(pubCo._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
