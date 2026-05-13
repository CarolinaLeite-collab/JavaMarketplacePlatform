package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.AuctionId;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionRepo extends IRepository<AuctionId, Auction> {

}
