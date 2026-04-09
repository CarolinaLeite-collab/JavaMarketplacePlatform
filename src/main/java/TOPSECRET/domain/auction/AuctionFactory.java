package TOPSECRET.domain.auction;

import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Factory responsible for creating {@link Auction} instances.
 * <p>
 * This class encapsulates the instantiation logic of {@code Auction},
 * centralizing object creation and isolating clients from constructor
 * details. Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */
public class AuctionFactory {

    public Auction createAuction(List<Item> item, Price startingPrice, Price reservePrice, ZonedDateTime auctionStartDate,
                                 ZonedDateTime auctionEndDate) {

            return new Auction(item, startingPrice, reservePrice, auctionStartDate, auctionEndDate);

    }

    public Auction createAuction(List<Item> item, Price startingPrice, Price reservePrice , Price outrightPrice,
                                 ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {

            return new Auction(item, startingPrice, reservePrice, outrightPrice, auctionStartDate, auctionEndDate);

    }
}
