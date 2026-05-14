package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;

import java.util.Collection;
import java.util.List;

public interface IItemRepo extends IRepository<ItemId, Item> {
    List<Item> findByIdInOrderByDescriptionAsc(Collection<String> ids);
}
