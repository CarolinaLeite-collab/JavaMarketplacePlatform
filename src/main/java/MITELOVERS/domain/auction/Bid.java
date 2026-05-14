package MITELOVERS.domain.auction;

import MITELOVERS.ddd.DomainEntity;
import MITELOVERS.domain.valueobject.BidId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.UserId;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A bid is a monetary offer ('offerPrice') place by a 'bidder' during an 'auction'
 * A 'Bid' contains:
 * - 'bidder': user who placed the `bid`
 * - 'offerPrice': the amount of money offered in a single `bid` for the item
 * - 'bidDate': timestamp of when the bid was placed
 *
 *  A `bid` is immutable; once a bid is placed, it cannot be modified.
 */

public class Bid implements DomainEntity<BidId> {

    private final UserId _userId;
    private final Price _offerPrice;
    private final Instant _bidDate;
    private final BidId _bidId;

    Bid(UserId userId, Price offerPrice) {
        this(userId, offerPrice, Clock.systemDefaultZone());
    }

    Bid(UserId userId, Price offerPrice, Clock clock) {
        validateBidder(userId);
        validateOfferPrice(offerPrice);

        if (clock == null) {
            throw new IllegalArgumentException ("Clock cannot be null");
        }

        _userId = userId;
        _offerPrice = offerPrice;
        _bidDate = Instant.now(clock);
        _bidId = BidId.newId();
    }

    Bid(UserId userId, Price offerPrice, Instant bidDate, BidId bidId) {
        validateBidder(userId);
        validateOfferPrice(offerPrice);

        _userId = userId;
        _offerPrice = offerPrice;
        _bidDate = Objects.requireNonNull(bidDate, "Bid date cannot be null");
        _bidId = Objects.requireNonNull(bidId, "BidId cannot be null");
    }

    public UserId getUserId() {
        return _userId;
    }

    public Price getOfferPrice() {
        return _offerPrice;
    }

    public Instant getBidDate() {
        return _bidDate;
    }

    private void validateBidder(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Bidder cannot be null");
        }
    }

    private void validateOfferPrice(Price offerPrice){
        if (offerPrice == null) {
            throw new IllegalArgumentException("Offer Price cannot be null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(_userId, bid._userId) &&
                Objects.equals(_offerPrice, bid._offerPrice) &&
                Objects.equals(_bidDate, bid._bidDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_userId, _offerPrice, _bidDate);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                        .withZone(ZoneId.systemDefault());

        return String.format("Bid{id=%s, userId=%s, offerPrice=%s, date=%s}",
                _bidId,
                _userId,
                _offerPrice,
                formatter.format(_bidDate));
    }

    @Override
    public BidId identity() {
        return _bidId;
    }

    @Override
    public boolean sameAs(Object object) {
        if (this == object) return true;
        if (!(object instanceof Bid other)) return false;
        return _bidId.equals(other._bidId);
    }

}
