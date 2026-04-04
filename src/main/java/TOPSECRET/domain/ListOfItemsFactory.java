package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.UserId;

/**
 * Factory responsible for creating {@link ListOfItems} instances.
 * @throws IllegalArgumentException if the list name is null, as enforced by {@link ListOfItems}'s constructor.
 */
public class ListOfItemsFactory {

    public ListOfItems createListOfItems(UserId userId, String name, Genre genre) {
        return new ListOfItems(userId, name, genre);
    }

    public ListOfItems createPublicListOfItems(UserId userId, String name, Genre genre) {
        ListOfItems list = new ListOfItems(userId, name, genre);
        list.makePublic();
        return list;
    }
}
