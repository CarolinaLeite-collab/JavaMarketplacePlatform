package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

public class PublicationTypeId implements DomainId {

    private final String _id;

    public PublicationTypeId(String publicationTypeName) {
        if (publicationTypeName == null || publicationTypeName.isBlank()) {
            throw new IllegalArgumentException("PublicationTypeId cannot be null or blank");
        }
        _id = publicationTypeName.trim().toUpperCase();
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
