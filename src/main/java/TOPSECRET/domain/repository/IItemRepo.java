package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.domain.valueobject.Description;
import TOPSECRET.domain.valueobject.EditionId;
import TOPSECRET.domain.valueobject.ItemId;

import java.util.List;

public interface IItemRepo extends IRepository<ItemId, Item> {

    ItemId addItem(EditionId editionId, Condition condition, Description description);

    List<ItemId> getDifferentOf(List<ItemId> existentItemIds);

    List<ItemId> findAllKeys();
}
