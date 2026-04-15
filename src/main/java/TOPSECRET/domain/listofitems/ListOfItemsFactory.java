package TOPSECRET.domain.listofitems;

import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.ListOfItemsId;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Factory responsible for creating {@link ListOfItems} instances.
 * @throws IllegalArgumentException if the userId, list name and/or genreId are null, as enforced by {@link ListOfItems}'s constructor.
 */

public class ListOfItemsFactory {

    public ListOfItems createListOfItems(UserId userId, String name, GenreId genreId) {

        ListOfItemsId listOfItemsId = ListOfItemsId.newId();

        return new ListOfItems(listOfItemsId, userId, name, genreId);
    }

    public ListOfItems createPublicListOfItems(UserId userId, String name, GenreId genreId) {

        ListOfItems list = createListOfItems(userId, name, genreId);
        list.makePublic();
        return list;
    }
}

