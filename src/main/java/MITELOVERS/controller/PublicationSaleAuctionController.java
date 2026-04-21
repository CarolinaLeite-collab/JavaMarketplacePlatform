package MITELOVERS.controller;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.SaleStatus;
import MITELOVERS.domain.valueobject.UserId;

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
    private  IAuctionRepo _iAuctionRepo;
    private  Library _library;
    private  IItemRepo _itemRepo;

    public PublicationSaleAuctionController(ILibraryRepo iLibraryRepo, IAuctionRepo iAuctionRepo, IItemRepo itemRepo, UserId userId) {

        _iLibraryRepo = iLibraryRepo;
        _iAuctionRepo = iAuctionRepo;
        _itemRepo = itemRepo;

    }

    public List<ItemId> getLibraryItemsIdList(UserId userId) {

        Library userLibrary = _iLibraryRepo.findLibraryByUserId(userId);

        List<ItemId> itemIds = userLibrary.getItemsIdInLibrary();
        return List.copyOf(itemIds);
    }

    public Auction putItemOnAuction(IItemRepo iItemRepo, List<ItemId> itemsId, Price startPrice, Price reservePrice, Price outrightPrice, ZonedDateTime startDate, ZonedDateTime endDate) {

        for (ItemId itemId : itemsId) {

            Item item = _itemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

            if (item.getSaleStatus() != SaleStatus.NotOnSale) {
                throw new IllegalStateException(itemId + " is already on sale!");
            }
        }

        Auction auction = _iAuctionRepo.addAuction(
                itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate
        );

        for (ItemId itemId : itemsId) {

            Item item = _itemRepo.ofIdentity(itemId).get();
            item.markAsAuction();
        }

        return auction;
    }
}
