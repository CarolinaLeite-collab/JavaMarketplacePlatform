package TOPSECRET.controller;

import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.SaleStatus;
import TOPSECRET.domain.valueobject.UserId;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * <h3>Controller responsible for handling publication auction operations (US016). </h3>
 * Separated into two steps:
 * <ol>
 *     <li>lookup of user's list of publications in their library (immutable copy)</li>
 *     <li>retrieval of actual publication, item creation, auction setup, and link between item and auction</li>
 * </ol>
 */

public class PublicationSaleAuctionController {

    private final ILibraryRepo _iLibraryRepo;
    private final IAuctionRepo _iAuctionRepo;
    private final Library _library;
    private final IItemRepo _itemRepo;

    public PublicationSaleAuctionController(ILibraryRepo iLibraryRepo, IAuctionRepo iAuctionRepo, Library library, IItemRepo iItemRepo, UserId userId) {

        _iLibraryRepo = iLibraryRepo;
        _iAuctionRepo = iAuctionRepo;
        _itemRepo = iItemRepo;
        _library = library;

    }

    public List<ItemId> getLibraryItemsIdList(UserId userId) {

        Library userLibrary = _iLibraryRepo.findLibraryByUserId(userId);

        List<ItemId> itemIds = userLibrary.getItemsIdInLibrary();
        return List.copyOf(itemIds);
    }

    public Auction putItemOnAuction(IItemRepo iItemRepo, List<ItemId> itemsId, Price startPrice, Price reservePrice, Price outrightPrice, ZonedDateTime startDate, ZonedDateTime endDate) {

        for (ItemId itemId : itemsId) {

            Item item = iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

            if (item.get_saleStatus() != SaleStatus.NotOnSale) {
                throw new IllegalStateException(itemId + " is already on sale!");
            }
        }

        Auction auction = _iAuctionRepo.addAuction(
                itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate
        );

        for (ItemId itemId : itemsId) {

            Item item = iItemRepo.ofIdentity(itemId).get();
            item.markAsAuction();
        }

        return auction;
    }
}
