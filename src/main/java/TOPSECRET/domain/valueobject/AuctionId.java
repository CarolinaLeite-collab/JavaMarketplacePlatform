package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;
import TOPSECRET.domain.auction.Auction;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents the unique identifier of an {@link Auction} in the domain.
 * <p>
 * Each {@code AuctionId} is automatically generated upon instantiation,
 * with the format {@code "AU-" + 8-character uppercase UUID substring}.
 * This ensures that each auction has a unique and easily recognizable identifier.
 * </p>
 */

 public class AuctionId implements DomainId {

    private String _id;

    public AuctionId() {

        _id = "AU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public AuctionId(String id) {
        _id = id;
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
