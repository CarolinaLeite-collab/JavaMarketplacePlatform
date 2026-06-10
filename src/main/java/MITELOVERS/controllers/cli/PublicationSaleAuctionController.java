package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.AuctionService;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.*;
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

@Controller
public class PublicationSaleAuctionController {

    private final AuctionService _auctionService;

    public PublicationSaleAuctionController(AuctionService service) {
        _auctionService = service;
    }

    public List<ItemId> getLibraryItemsIdList(UserId userId) {

        return _auctionService.getLibraryItemsIdList(userId);
    }

    public Auction putItemOnAuction(List<ItemId> itemsId, Price startPrice, Price reservePrice, Price outrightPrice,
                                    ZonedDateTime startDate, ZonedDateTime endDate) {

        return _auctionService.putItemOnAuction(itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate);
    }

    public Auction putItemOnAuction(List<ItemId> itemsId, Price startPrice, Price reservePrice,
                                    ZonedDateTime startDate, ZonedDateTime endDate) {
        return _auctionService.putItemOnAuction(itemsId, startPrice, reservePrice, startDate, endDate);
    }
}
