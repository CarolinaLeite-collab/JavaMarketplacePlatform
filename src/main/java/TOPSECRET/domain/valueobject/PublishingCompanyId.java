package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

public class PublishingCompanyId implements DomainId {

    private final String _id;

    public PublishingCompanyId(String publishingCompanyName) {
        if (publishingCompanyName == null || publishingCompanyName.isBlank()) {
            throw new IllegalArgumentException("PublishingCompanyId cannot be null or blank");
        }
        _id = publishingCompanyName.trim().toUpperCase();
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
