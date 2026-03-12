package TOPSECRET.domain;

/**
 * Factory responsible for creating {@link ListOfPublications} instances.
 * @throws IllegalArgumentException if the list name is null, as enforced by {@link ListOfPublications}'s constructor.
 */
public class ListOfPublicationsFactory {

    public ListOfPublications createListOfPublications(User user, String name, Genre genre) {
        return new ListOfPublications(user, name, genre);
    }

    public ListOfPublications createPublicListOfPublications(User user, String name, Genre genre) {
        ListOfPublications list = new ListOfPublications(user, name, genre);
        list.makePublic();
        return list;
    }
}
