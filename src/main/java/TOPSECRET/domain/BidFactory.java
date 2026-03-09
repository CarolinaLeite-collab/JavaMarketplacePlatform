package TOPSECRET.domain;
/**
 * Factory responsible for creating {@link Bid} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */

public class BidFactory {
    public Bid createBid(User bidder, Price offerPrice){
        return new  Bid(bidder, offerPrice);
    }
}
