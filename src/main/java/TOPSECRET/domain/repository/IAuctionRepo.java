package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.valueobject.AuctionId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.List;

public interface IAuctionRepo extends IRepository<AuctionId, Auction> {

    Auction addAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice, Price outrightPrice,
                       ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

    Auction addAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice,
                       ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

    Auction createAuction(ItemId itemId, Price startingPrice, Price reservePrice, Price outrightPrice,
                          ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

}
