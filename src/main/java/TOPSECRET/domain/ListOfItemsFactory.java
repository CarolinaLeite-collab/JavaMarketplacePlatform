package TOPSECRET.domain;

/**
 * Factory responsible for creating {@link ListOfItems} instances.
 * @throws IllegalArgumentException if the list name is null, as enforced by {@link ListOfItems}'s constructor.
 */
public class ListOfItemsFactory {

    public ListOfItems createListOfItems(User user, String name, Genre genre) {
        return new ListOfItems(user, name, genre);
    }

    public ListOfItems createPublicListOfItems(User user, String name, Genre genre) {
        ListOfItems list = new ListOfItems(user, name, genre);
        list.makePublic();
        return list;
    }
}
