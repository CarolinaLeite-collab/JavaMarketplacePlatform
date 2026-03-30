package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;


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

