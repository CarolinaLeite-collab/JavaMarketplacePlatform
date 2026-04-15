package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.listofitems.ListOfItems;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.ListOfItemsId;
import TOPSECRET.domain.valueobject.UserId;

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

