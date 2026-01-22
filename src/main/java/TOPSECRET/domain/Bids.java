package TOPSECRET.domain;

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

public class Bids {

    private List<Bid> _list;

    //public constructor
    public Bids() {
        _list = new ArrayList<>();
    }

    public void addBid(Bid bid) {
        _list.add(Objects.requireNonNull(bid, "Bid must not be null"));
    // validate that same value list is not already on the list
    }

    //methode to get Highest Bid
    public Bid getHighestBid() {

        if (_list.isEmpty()) {
            throw new IllegalStateException("No bids available");
        }

        Bid higherBid = _list.get(0);

        for (int i = 1; i < _list.size(); i++) {
            Bid otherBid = _list.get(i);
            if (otherBid.getOfferPrice().getValue() > higherBid.getOfferPrice().getValue()) {
                higherBid = otherBid;
            }
        }
        return higherBid;
    }

}



