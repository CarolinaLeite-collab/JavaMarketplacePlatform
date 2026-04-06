package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

import java.util.Objects;
import java.util.UUID;

public class AuctionId implements DomainId {

    private final UUID _id;

    public AuctionId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        _id = id;
    }

    public static AuctionId createId() {
        return new AuctionId(UUID.randomUUID());
    }

    public UUID getId() {
        return _id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuctionId other)) return false;
        return _id.equals(other._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    public String toString() {
        return _id.toString();
    }
}
