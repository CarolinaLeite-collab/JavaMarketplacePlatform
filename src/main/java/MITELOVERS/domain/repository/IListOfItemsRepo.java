package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.List;

public interface IListOfItemsRepo extends IRepository<ListOfItemsId, ListOfItems> {

    List<ListOfItems> findListOfItemsByUserId(UserId userId);
}

