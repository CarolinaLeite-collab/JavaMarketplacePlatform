package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.AuctionId;

public interface IAuctionRepo extends IRepository<AuctionId, Auction> {

}
