package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.valueobject.Price;

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

    public PublicationSaleAuctionController(ILibraryRepo libraryRepo, IAuctionRepo iAuctionRepo, Library library) {
        if (libraryRepo == null || iAuctionRepo == null || library == null) {
            throw new NullPointerException("Repositories and factories are required");
        }
        _iLibraryRepo = libraryRepo;
        _iAuctionRepo = iAuctionRepo;
        _library = library;

    }

    // US016 Controller Step 1: getLibraryItemList(user)
   public List<Item> getLibraryItemsList(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User required");
        }
        Library userLibrary = _iLibraryRepo.findLibraryByUser(user);
        List<Item> items = userLibrary.getItemsInLibrary();
        return List.copyOf(items); //This is an immutable list
   }


//    }

    // US016 Controller Step 2: putItemOnAuction
    // returns true if publication successfully put on sale in auction
    public Auction putItemOnAuction(Item item, Price startPrice, Price outrightPrice, ZonedDateTime startDate, ZonedDateTime endDate) throws IllegalArgumentException {
        if (item == null || startPrice == null || startDate== null || endDate == null) {
            return null;
        }
        if (endDate.isBefore(startDate)) {
            return null;
        }

        Item itemForAuction = _library.getItem(item);

        try {
            Auction newAuction = _iAuctionRepo.createAuction(itemForAuction, startPrice, outrightPrice, startDate, endDate);
            itemForAuction.setAuction(newAuction);
            return newAuction;
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }

}
