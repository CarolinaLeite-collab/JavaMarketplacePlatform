package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;

public interface IBidRepo {

    Bid createBid(User bidder, Price offerPrice);
    Bid getHighestBid();

}
