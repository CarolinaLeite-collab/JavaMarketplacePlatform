package MITELOVERS.persistence.mem;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.valueobject.AuctionId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Repository class responsible for managing auctions within the system.
 * <p>
 * The {@code AuctionRepo} class provides methods to create new auctions
 * and to retrieve auction items that match a given genre.
 * Internally, it maintains an in-memory list of ongoing auctions.
 * </p>
 */

@Repository
@Profile("mem")
public class MemAuctionRepo implements IAuctionRepo {

    private final Map<AuctionId, Auction> DATA = new HashMap<>();


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
}
