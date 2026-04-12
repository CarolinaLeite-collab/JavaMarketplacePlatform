package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Factory responsible for creating {@link Bid} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */

public class BidFactory {
    public Bid createBid(UserId userId, Price offerPrice){
        return new Bid(userId, offerPrice);
    }
}
