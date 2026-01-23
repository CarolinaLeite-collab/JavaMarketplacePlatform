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
    private final PublicationRepo _publicationRepo;
    private final ItemRepo _itemRepo;
    private final AuctionRepo _auctionRepo;

    public PublicationSaleAuctionController(LibraryRepo libraryRepo, PublicationRepo publicationRepo, ItemRepo itemRepo, AuctionRepo auctionRepo) {

        _libraryRepo = libraryRepo;
        _publicationRepo = publicationRepo;
        _itemRepo = itemRepo;
        _auctionRepo = auctionRepo;

    }

    // US016 Controller Step 1: getLibraryPublicationList(user)
   public List<PublicationDetails> getLibraryPublicationList(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User required");
        }
        Library userLibrary = _libraryRepo.findByUser(user);
        return userLibrary.getPublicationsInLibrary(); //This is an immutable list
   }

   // US016 Controller Step 2: putPublicationOnAuction(publication, condition, ...)
      // returns true if publication successfully put on sale in auction
    public boolean putPublicationOnAuction(Publication publication, Condition condition, Price startPrice, ZonedDateTime startDate, ZonedDateTime endDate) {

        if (publication == null || condition == null || startPrice == null || startDate == null || endDate == null) {
            return false; //If one of the parameters is not provided, cannot put publication on sale for auction
        }
        if (endDate.isBefore(startDate)) {
            return false; // End date cannot be before start date
        }
        if (_itemRepo.exists(publication)){
            return false; //If publication was already made into an item put on sale, cannot duplicate
        }

        // Following US016 SD flow: get real publication -> create Item -> Auction -> link between item and its auction
        Publication actualPublication = _publicationRepo.getPublication(publication);
        Item item = _itemRepo.createItem(actualPublication, condition);
        Auction auction = _auctionRepo.createAuction(item, startPrice, startDate, endDate);
        item.setAuction(auction);
        return true;
    }

}
