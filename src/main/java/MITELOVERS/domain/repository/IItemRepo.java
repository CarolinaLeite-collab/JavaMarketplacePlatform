package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.valueobject.ItemId;

public interface IItemRepo extends IRepository<ItemId, Item> {

}
