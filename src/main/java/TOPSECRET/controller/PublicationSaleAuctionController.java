package TOPSECRET.controller;

import TOPSECRET.domain.Auction;
import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.Price;
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

    public PublicationSaleAuctionController(ILibraryRepo iLibraryRepo, IAuctionRepo iAuctionRepo, Library library, UserId userId) {

        _iLibraryRepo = iLibraryRepo;
        _iAuctionRepo = iAuctionRepo;
        _library = library;

    }

   public List<Item> getLibraryItemsList(UserId userId) {

        Library userLibrary = _iLibraryRepo.findLibraryByUserId(userId);

        List<Item> items = userLibrary.getItemsInLibrary();
        return List.copyOf(items);
   }

    public Auction putItemOnAuction(Item item, Price startPrice, Price outrightPrice, ZonedDateTime startDate, ZonedDateTime endDate) {

        Item itemForAuction = _library.getItem(item);

        Auction newAuction = _iAuctionRepo.createAuction(itemForAuction, startPrice, outrightPrice, startDate, endDate);
        itemForAuction.setAuction(newAuction);
        return newAuction;
    }
}
