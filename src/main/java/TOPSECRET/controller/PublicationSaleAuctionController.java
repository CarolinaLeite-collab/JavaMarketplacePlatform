package TOPSECRET.controller;

import TOPSECRET.domain.*;
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

    private final LibraryRepo _libraryRepo;
    private final ItemRepo _itemRepo;
    private final AuctionRepo _auctionRepo;
    private final ItemFactory _itemFactory;
    private final AuctionFactory _auctionFactory;

    public PublicationSaleAuctionController(LibraryRepo libraryRepo, ItemRepo itemRepo, AuctionRepo auctionRepo,
                                            ItemFactory itemFactory, AuctionFactory auctionFactory) {
        if (libraryRepo == null || itemRepo == null || auctionRepo == null || itemFactory == null || auctionFactory == null) {
            throw new NullPointerException("Repositories and factories are required");
        }
        _libraryRepo = libraryRepo;
        _itemRepo = itemRepo;
        _auctionRepo = auctionRepo;
        _itemFactory = itemFactory;
        _auctionFactory = auctionFactory;

    }

    // US016 Controller Step 1: getLibraryPublicationList(user)
   public List<PublicationDetails> getLibraryPublicationList(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User required");
        }
        Library userLibrary = _libraryRepo.findLibraryByUser(user);
        return userLibrary.getPublicationsInLibrary(); //This is an immutable list
   }

   // US016 Controller Step 2: putPublicationOnAuction(publication, condition, ...)
      // returns true if publication successfully put on sale in auction
    public Auction putPublicationOnAuction(User user, Publication publication, Condition condition, Price startPrice, ZonedDateTime startDate, ZonedDateTime endDate) {

        if (user == null || publication == null || condition == null || startPrice == null || startDate == null || endDate == null) {
            return null; //If one of the parameters is not provided, cannot put publication on sale for auction
        }
        if (endDate.isBefore(startDate)) {
            return null; // End date cannot be before start date
        }
        if (_itemRepo.exists(publication)){
            return null; //If publication was already made into an item put on sale, cannot duplicate
        }

        // Following US016 SD flow: get real publication from user's library -> create Item -> create Auction -> link between item and its auction
        Library userLibrary = _libraryRepo.findLibraryByUser(user);
        Publication actualPublicationInLibrary = userLibrary.getPublicationFromLibrary(publication);
        Item item = _itemFactory.createItem(actualPublicationInLibrary, condition);
        Auction auction = _auctionFactory.createAuction(item, startPrice, startDate, endDate);
        item.setAuction(auction);
        return auction;
    }

}
