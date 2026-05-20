package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.ItemId;

import java.util.List;

public interface IAuctionRepo extends IRepository<AuctionId, Auction> {

    List<ItemId> findByItemsIdSorted(List<ItemId> itemIds);

}
