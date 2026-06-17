package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.AuctionService;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Controller responsible for handling auction-related user operations.
 * <p>
 * This controller serves as an intermediary between the presentation layer
 * and the {@link AuctionService}, delegating requests related to auction
 * creation and retrieval of library items.
 * </p>
 *
 */

@AllArgsConstructor
@Controller
public class PublicationSaleAuctionController {

    private final AuctionService _auctionService;


    public Auction putItemOnAuction(List<ItemId> itemsId, Price startPrice, Price reservePrice, Price outrightPrice,
                                    ZonedDateTime startDate, ZonedDateTime endDate, UserId seller) {

        return _auctionService.putItemOnAuction(itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate, seller);
    }

    public Auction putItemOnAuction(List<ItemId> itemsId, Price startPrice, Price reservePrice,
                                    ZonedDateTime startDate, ZonedDateTime endDate, UserId seller) {
        return _auctionService.putItemOnAuction(itemsId, startPrice, reservePrice, startDate, endDate, seller);
    }
}
