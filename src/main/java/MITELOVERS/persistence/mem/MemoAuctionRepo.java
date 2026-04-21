package MITELOVERS.persistence.mem;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Repository class responsible for managing auctions within the system.
 * <p>
 * The {@code AuctionRepo} class provides methods to create new auctions
 * and to retrieve auction items that match a given genre.
 * Internally, it maintains an in-memory list of ongoing auctions.
 * </p>
 */

public class MemoAuctionRepo implements IAuctionRepo {

    private final Map<AuctionId, Auction> DATA;
    private final AuctionFactory _auctionFactory;

    MemoAuctionRepo(AuctionFactory auctionFactory) {
        DATA = new HashMap<>();
        _auctionFactory = auctionFactory;
    }

    @Override
    public Auction save(Auction auction) {
        DATA.put(auction.identity(), auction);
        return auction;
    }

    @Override
    public Iterable<Auction> findAll() {
        return DATA.values();
    }

    @Override
    public List<AuctionId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());
    }

    @Override
    public Optional<Auction> ofIdentity(AuctionId id) {
        if (!containsOfIdentity(id)) {
            return Optional.empty();
        } else {
            return Optional.of(DATA.get(id));
        }
    }

    @Override
    public boolean containsOfIdentity(AuctionId id) {
        return DATA.containsKey(id);
    }

    @Override
    public Auction addAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice,
                              Price outrightPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        Auction auction = _auctionFactory.createAuction(itemsId, startingPrice, reservePrice,
                outrightPrice, auctionStartDate, auctionEndDate);
        return save(auction);
    }

    @Override
    public Auction addAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice,
                              ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        return addAuction(itemsId, startingPrice, reservePrice, null, auctionStartDate, auctionEndDate);
    }
}
