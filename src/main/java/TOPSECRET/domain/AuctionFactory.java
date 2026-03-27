package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;

/**
 * Factory responsible for creating {@link Auction} instances.
 * <p>
 * This class encapsulates the instantiation logic of {@code Auction},
 * centralizing object creation and isolating clients from constructor
 * details. Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */
public class AuctionFactory {

    public Auction createAuction(Item item, Price startingPrice, ZonedDateTime auctionStartDate,
                                 ZonedDateTime auctionEndDate) {

            return new Auction(item, startingPrice, auctionStartDate, auctionEndDate);

    }

    public Auction createAuction(Item item, Price startingPrice, Price outrightPrice,
                          ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {

            return new Auction(item, startingPrice, outrightPrice, auctionStartDate, auctionEndDate);

    }
}
