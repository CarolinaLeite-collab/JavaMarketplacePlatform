package TOPSECRET.domain;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.domain.valueobject.Description;
import TOPSECRET.domain.valueobject.EditionId;
import TOPSECRET.domain.valueobject.ItemId;

import java.util.List;

public interface IItemRepo extends IRepository<ItemId, Item> {

    Item addItem(EditionId editionId, Condition condition, Description description);

    List<Item> getDifferentOf(List<Item> existentItems);
}
