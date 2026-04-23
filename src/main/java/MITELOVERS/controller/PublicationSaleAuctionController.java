package MITELOVERS.controller;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * <h3>Controller responsible for handling item auction operations. </h3>
 * Separated into two steps:
 * <ol>
 *     <li>lookup of user's list of items in their library (immutable copy)</li>
 *     <li>retrieval of actual item, auction setup, and link between item and auction</li>
 * </ol>
 */

public class PublicationSaleAuctionController {

    private final ILibraryRepo _iLibraryRepo;
    private IAuctionRepo _iAuctionRepo;
    private AuctionFactory _auctionFactory;
    //private Library _library;
    private IItemRepo _iItemRepo;

    public PublicationSaleAuctionController(ILibraryRepo iLibraryRepo, IAuctionRepo iAuctionRepo, AuctionFactory auctionFactory,
                                            IItemRepo iItemRepo, UserId userId) {

        _iLibraryRepo = iLibraryRepo;
        _iAuctionRepo = iAuctionRepo;
        _auctionFactory = auctionFactory;
        _iItemRepo = iItemRepo;

    }

    public List<ItemId> getLibraryItemsIdList(UserId userId) {

        LibraryId libraryID = LibraryId.fromUserId(userId);

        Library userLibrary = _iLibraryRepo.ofIdentity(libraryID)
                .orElseThrow(() -> new IllegalStateException("Library not found for user!"));

        List<ItemId> itemIds = userLibrary.getItemsIdInLibrary();
        return List.copyOf(itemIds);
    }

    public Auction addAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice,
                              Price outrightPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {

        Auction auction = _auctionFactory.createAuction(itemsId, startingPrice, reservePrice,
                outrightPrice, auctionStartDate, auctionEndDate);

        return _iAuctionRepo.save(auction);
    }

    public Auction addAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice,
                              ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {

        return addAuction(itemsId, startingPrice, reservePrice, null, auctionStartDate, auctionEndDate);
    }

    public Auction putItemOnAuction(List<ItemId> itemsId, Price startPrice, Price reservePrice, Price outrightPrice, ZonedDateTime startDate, ZonedDateTime endDate) {

        for (ItemId itemId : itemsId) {

            Item item = _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

            if (item.getSaleStatus() != SaleStatus.NotOnSale) {
                throw new IllegalStateException(itemId + " is already on sale!");
            }
        }

        Auction auction = addAuction(
                itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate
        );

        for (ItemId itemId : itemsId) {

            Item item = _iItemRepo.ofIdentity(itemId).get();
            item.markAsAuction();
        }

        return auction;
    }

    public Auction putItemOnAuction(List<ItemId> itemsId, Price startPrice, Price reservePrice,
                                    ZonedDateTime startDate, ZonedDateTime endDate) {
        return putItemOnAuction(itemsId, startPrice, reservePrice, null, startDate, endDate);
    }
}
