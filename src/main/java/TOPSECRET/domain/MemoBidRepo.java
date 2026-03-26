package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a collection of {@link Bid} instances for an auction or item.
 * <p>
 * Provides functionality to add bids and to retrieve the highest bid based on the offered price.
 * Ensures that bids are not null when added.
 * </p>
 */

public class MemoBidRepo implements IBidRepo {

    private List<Bid> _bids;
    private BidFactory _bidFactory;

    //public constructor
    public MemoBidRepo(BidFactory bidFactory) {
        _bids = new ArrayList<>();
        _bidFactory = bidFactory;
    }

    @Override
    public Bid createBid(User bidder, Price offerPrice) {
        Bid bid = _bidFactory.createBid(bidder, offerPrice);

        _bids.add(Objects.requireNonNull(bid, "Bid must not be null"));

        return bid;
    }

    public void addBid(Bid bid) {
        _bids.add(bid);
    }

    //method to get Highest Bid
    @Override
    public Bid getHighestBid() {

        if (_bids.isEmpty()) {
            throw new IllegalStateException("No bids available");
        }

        Bid higherBid = _bids.get(0);

        for (int i = 1; i < _bids.size(); i++) {
            Bid otherBid = _bids.get(i);
            if (otherBid.getOfferPrice().getValue() > higherBid.getOfferPrice().getValue()) {
                higherBid = otherBid;
            }
        }
        return higherBid;
    }

}



