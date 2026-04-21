package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.List;
import java.util.Map;

public interface IListOfItemsRepo extends IRepository<ListOfItemsId, ListOfItems> {

    ListOfItems addListOfItems(UserId userId, String name, GenreId genreId);

    List<ListOfItems> findPublicListsByGenre(GenreId genreId);

    List<ListOfItems> findListsByUserId(UserId userId);

    ListOfItems findByOwnerNameAndGenre(UserId userId, String name, GenreId genreId);

    // Temporary
    Map<ListOfItemsId, String> getIdNameMap();
}

