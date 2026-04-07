package TOPSECRET.domain;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.valueobject.Price;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A bid is a monetary offer ('offerPrice') place by a 'bidder' during an 'auction'
 * A 'Bid' contains:
 * - The 'bidder' (User who placed the bid)
 * - The 'offerPrice', which is the amount of money offered
 * - The 'bidDate', timestamp when the bid was placed
 *
 *  This class is immutable - once a bid is placed, it cannot be modified.
 */

public class Bid {

    private final User _bidder;
    private final Price _offerPrice;
    private final Instant _bidDate;

    Bid(User bidder, Price offerPrice) {
        this(bidder, offerPrice, Clock.systemDefaultZone());
    }

    Bid(User bidder, Price offerPrice, Clock clock) {
        validateBidder(bidder);
        validateOfferPrice(offerPrice);

        if (clock == null) {
            throw new IllegalArgumentException ("Clock cannot be null");
        }

        _bidder = bidder;
        _offerPrice = offerPrice;
        _bidDate = Instant.now(clock);
    }

    public User getBidder() {
        return _bidder;
    }

    public Price getOfferPrice() {
        return _offerPrice;
    }

    public Instant getBidDate() {
        return _bidDate;
    }

    private void validateBidder(User bidder) {
        if (bidder == null) {
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
        return Objects.equals(_bidder, bid._bidder) &&
                Objects.equals(_offerPrice, bid._offerPrice) &&
                Objects.equals(_bidDate, bid._bidDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_bidder, _offerPrice, _bidDate);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                        .withZone(ZoneId.systemDefault());

        return String.format("Bid{bidder=%s, offerPrice=%s, date=%s}",
                _bidder,
                _offerPrice,
                formatter.format(_bidDate));
    }

}
