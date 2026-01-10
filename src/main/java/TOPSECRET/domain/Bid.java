package TOPSECRET.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;



public class Bid {


    /**
     * A bid is a monetary offer ('offerPrice') place by a 'bidder' during an 'auction'
     * A 'Bid' contains:
     * - The 'bidder' (User who placed the bid)
     * - The 'offerPrice', which is the amount of money offered
     * - The 'bidDate', timestamp when the bid was placed
     *
     *  This class is immutable - once a bid is placed, it cannot be modified.
     */

    private final User _bidder; // Final: bidder cannot change
    private final Price _offerPrice; // Final: offer price is immutable
    private final LocalDateTime _bidDate; //Final: historical timestamp


    /**
     * Creates a new Bid with the current timestamp.
     *
     * @param bidder The user placing the bid
     * @param offerPrice The amount being offered
     * @throws IllegalArgumentException if validation fails
     */


    public Bid(User bidder, Price offerPrice) {
        validateBidder(bidder);
        validateOfferPrice(offerPrice);

        _bidder = bidder;
        _offerPrice = offerPrice;
        _bidDate = LocalDateTime.now();
    }

    //Getters

    /**
     * Returns the user who placed this bid.
     * @return the bidder
     */
    public User getBidder() {
        return _bidder;
    }

    /**
     * Returns the monetary amount offered in this bid.
     * @return the offer price
     */
    public Price getOfferPrice() {
        return _offerPrice;
    }

    /**
     * Returns when this bid was placed.
     * @return the bid date
     */
    public LocalDateTime getBidDate() {
        return _bidDate;
    }

    //Validation Method


    private void validateBidder(User bidder) {
        if (bidder == null) {
            throw new IllegalArgumentException("Bidder cannot be null");
        }
    }

    private void validateOfferPrice(Price offerPrice){
        if (offerPrice == null) {
            throw new IllegalArgumentException("Offer Price cannot be null");
        }
        // Note: If Price class already validates that the value is positive
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("Bid{bidder=%s, offerPrice=%s, date=%s}",
                _bidder,
                _offerPrice,
                _bidDate.format(formatter));
    }

}
