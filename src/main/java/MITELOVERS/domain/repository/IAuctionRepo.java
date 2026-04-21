package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.List;

public interface IAuctionRepo extends IRepository<AuctionId, Auction> {

    Auction addAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice, Price outrightPrice,
                       ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

    Auction addAuction(List<ItemId> itemsId, Price startingPrice, Price reservePrice,
                       ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);
}
