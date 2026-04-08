package TOPSECRET.domain;

import TOPSECRET.ddd.DomainId;

import java.util.UUID;

public class BidId implements DomainId {

    private final UUID _id;


    public BidId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("BidId cannot be null");
        }
        _id = id;
    }

    public static BidId newId() {
        return new BidId(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BidId other)) return false;
        return _id.equals(other._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    @Override
    public String toString() {
        return _id.toString();
    }
}
