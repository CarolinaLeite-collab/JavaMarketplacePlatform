package TOPSECRET.domain.auction;

import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Factory responsible for creating {@link Auction} instances.
 * <p>
 * This class encapsulates the instantiation logic of {@code Auction},
 * centralizing object creation and isolating clients from constructor
 * details.
 */
public class AuctionFactory {

    public Auction createAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice,
                                 ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        return new Auction(itemsId, startingPrice, reservePrice, auctionStartDate, auctionEndDate);
    }

    public Auction createAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice, Price outrightPrice,
                                 ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        return new Auction(itemsId, startingPrice, reservePrice, outrightPrice, auctionStartDate, auctionEndDate);
    }

    public Auction createAuction(ItemId itemId, Price startingPrice, Price reservePrice, Price outrightPrice,
                                 ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        return new Auction(List.of(itemId), startingPrice, reservePrice, outrightPrice, auctionStartDate, auctionEndDate);
    }
}
