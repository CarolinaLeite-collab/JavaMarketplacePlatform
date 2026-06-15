package MITELOVERS.applicationservices;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.SaleStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Application service responsible for managing auction- and bid-related operations.
 * <p>
 * This service coordinates the interaction between repositories and domain
 * objects required to create auctions and bids, along with placing library items on auction.
 * It validates item availability, creates and persists auctions, creates and persists
 * bids on an auction, and updates the sale status of the involved items.
 * </p>
 */

@Service
@AllArgsConstructor
public class AuctionService {

    private final IAuctionRepo _iAuctionRepo;
    private final AuctionFactory _auctionFactory;
    private final IItemRepo _iItemRepo;

    @Transactional
    public Auction putItemOnAuction(List<ItemId> itemsId, Price startPrice, Price reservePrice, Price outrightPrice, ZonedDateTime startDate, ZonedDateTime endDate) {

        for (ItemId itemId : itemsId) {

            Item item = _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new NoSuchElementException("Item not found: " + itemId));

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

        return _iAuctionRepo.save(auction);
    }

    // === Bidding support ===

    // for returning both auction and bid
    public record BidPlacementResult(Auction auction, Bid bid) {}

    @Transactional
    public BidPlacementResult placeBid(String auctionIdRaw,
                                       UserId userId,
                                       Price offerPrice) {

        AuctionId auctionId = new AuctionId(auctionIdRaw);
        Auction auction = _iAuctionRepo.ofIdentity(auctionId)
                .orElseThrow(() -> new NoSuchElementException("Auction not found: " + auctionIdRaw));

        Bid bid = auction.placeBid(userId, offerPrice);

        _iAuctionRepo.save(auction);

        return new BidPlacementResult(auction, bid);
    }

}
