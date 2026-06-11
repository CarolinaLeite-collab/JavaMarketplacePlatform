package MITELOVERS.applicationservices;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Application service responsible for managing auction-related operations.
 * <p>
 * This service coordinates the interaction between repositories and domain
 * objects required to create auctions and place library items on auction.
 * It validates item availability, creates and persists auctions, and updates
 * the sale status of the involved items.
 * </p>
 */

@Service
@AllArgsConstructor
public class AuctionService {

    private final ILibraryRepo _iLibraryRepo;
    private final IAuctionRepo _iAuctionRepo;
    private final AuctionFactory _auctionFactory;
    private final IItemRepo _iItemRepo;

    @Transactional(readOnly = true)
    public List<ItemId> getLibraryItemsIdList(UserId userId) {

        LibraryId libraryID = LibraryId.fromUserId(userId);

        Library userLibrary = _iLibraryRepo.ofIdentity(libraryID)
                .orElseThrow(() -> new IllegalStateException("Library not found for user!"));

        List<ItemId> itemIds = userLibrary.getItemsIdInLibrary();
        return List.copyOf(itemIds);
    }

    @Transactional
    public Auction putItemOnAuction(List<ItemId> itemsId, Price startPrice, Price reservePrice, Price outrightPrice, ZonedDateTime startDate, ZonedDateTime endDate) {

        List<Item> items = new ArrayList<>();

        for (ItemId itemId : itemsId) {

            Item item = _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalStateException("Item not found: " + itemId));

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
            _iItemRepo.save(item);
        }

        return auction;
    }

    @Transactional
    public Auction putItemOnAuction(List<ItemId> itemsId, Price startPrice, Price reservePrice,
                                    ZonedDateTime startDate, ZonedDateTime endDate) {
        return putItemOnAuction(itemsId, startPrice, reservePrice, null, startDate, endDate);
    }


    private Auction addAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice,
                               Price outrightPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {

        Auction auction = _auctionFactory.createAuction(itemsId, startingPrice, reservePrice,
                outrightPrice, auctionStartDate, auctionEndDate);

        if (_iAuctionRepo.containsOfIdentity(auction.identity())) {

            throw new IllegalStateException("Auction already exists!");

        }

        return _iAuctionRepo.save(auction);
    }

}
