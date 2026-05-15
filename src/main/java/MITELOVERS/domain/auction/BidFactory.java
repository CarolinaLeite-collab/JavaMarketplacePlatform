package MITELOVERS.domain.auction;

import MITELOVERS.domain.valueobject.BidId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Factory responsible for creating {@link Bid} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */

@Component
public class BidFactory {
    public Bid createBid(UserId userId, Price offerPrice){
        return new Bid(userId, offerPrice);
    }

    public Bid createBid(UserId userId, Price offerPrice, Instant bidDate, BidId bidId){
        return new Bid(userId, offerPrice, bidDate, bidId);
    }
}
