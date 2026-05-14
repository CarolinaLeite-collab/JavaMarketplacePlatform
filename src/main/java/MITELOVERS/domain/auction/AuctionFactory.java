package MITELOVERS.domain.auction;

import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Factory responsible for creating {@link Auction} instances.
 * <p>
 * This class encapsulates the instantiation logic of {@code Auction},
 * centralizing object creation and isolating clients from constructor
 * details.
 */

@Component
public class AuctionFactory {

    public Auction createAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice,
                                 ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        return new Auction(itemsId, startingPrice, reservePrice, auctionStartDate, auctionEndDate);
    }

    public Auction createAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice, Price outrightPrice,
                                 ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        return new Auction(itemsId, startingPrice, reservePrice, outrightPrice, auctionStartDate, auctionEndDate);
    }
}
