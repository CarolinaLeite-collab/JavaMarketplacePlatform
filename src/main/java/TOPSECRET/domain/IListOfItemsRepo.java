package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.UserId;

import java.util.List;

public interface IListOfItemsRepo {

    ListOfItems addListOfItems(UserId userId, String name, Genre genre);

    List<ListOfItems> getListOfListOfItems();

    List<ListOfItems> findPublicListsByGenre(Genre genre);

    List<ListOfItems> findListsByUserId(UserId userId);

    ListOfItems findByOwnerNameAndGenre(UserId userId, String name, Genre genre);
}
