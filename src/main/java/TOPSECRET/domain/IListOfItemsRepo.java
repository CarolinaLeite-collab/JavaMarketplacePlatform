package TOPSECRET.domain;

import TOPSECRET.domain.ListOfItems.ListOfItems;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.UserId;

import java.util.List;

public interface IListOfItemsRepo {

    ListOfItems addListOfItems(UserId userId, String name, GenreId genreId);

    List<ListOfItems> getListOfListOfItems();

    List<ListOfItems> findPublicListsByGenre(GenreId genreId);

    List<ListOfItems> findListsByUserId(UserId userId);

    ListOfItems findByOwnerNameAndGenre(UserId userId, String name, GenreId genreId);
}

