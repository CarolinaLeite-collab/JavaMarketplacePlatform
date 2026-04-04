package TOPSECRET.domain;

import java.util.List;

public interface IListOfItemsRepo {

    ListOfItems addListOfItems(User user, String name, Genre genre);

    List<ListOfItems> getListOfListOfItems();

    List<ListOfItems> findPublicListsByGenre(Genre genre);

    List<ListOfItems> findListsByUser(User user);

    ListOfItems findByOwnerNameAndGenre(User user, String name, Genre genre);
}
