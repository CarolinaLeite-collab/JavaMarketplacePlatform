package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link ListOfPublications} instances.
 * <p>
 * This class handles the creation, storage, and retrieval of private lists of publications.
 * It ensures that duplicate lists (same user, name, and genre) are not allowed.
 * </p>
 */

public class ListOfPublicationsRepo {
    private final List<ListOfPublications> _listsOfListOfPublications;
    private final ListOfPublicationsFactory _factory;

    public ListOfPublicationsRepo() {
        this(new ListOfPublicationsFactory());
    }

    public ListOfPublicationsRepo(ListOfPublicationsFactory factory) {
        _listsOfListOfPublications = new ArrayList<>();
        _factory = factory;
    }

    public ListOfPublications addListOfPublications(User user, String name, Genre genre) {
        ListOfPublications newList = _factory.createListOfPublications(user, name, genre);
        if (_listsOfListOfPublications.contains(newList)) {
            return null;
        }
        _listsOfListOfPublications.add(newList);
        return newList;
    }

    public List<ListOfPublications> getListOfListOfPublications() {
        return List.copyOf(_listsOfListOfPublications);
    }

    public List<ListOfPublications> findPublicListsByGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre is mandatory");
        }

        List<ListOfPublications> result = new ArrayList<>();
        for (ListOfPublications lop : _listsOfListOfPublications) {
            if (!lop.isPrivate() && lop.getGenre().equals(genre)) {
                result.add(lop);
            }
        }
        return List.copyOf(result);
    }

    public List<ListOfPublications> findListsByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is mandatory");
        }

        List<ListOfPublications> result = new ArrayList<>();
        for (ListOfPublications lop : _listsOfListOfPublications) {
            if (lop.getUser().equals(user)) {
                result.add(lop);
            }
        }
        return List.copyOf(result);
    }

    public ListOfPublications findByOwnerNameAndGenre(User user, String name, Genre genre) {
        if (user == null) {
            throw new IllegalArgumentException("User is mandatory");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("List name is mandatory");
        }
        if (genre == null) {
            throw new IllegalArgumentException("Genre is mandatory");
        }

        String normalizedName = name.trim();

        for (ListOfPublications lop : _listsOfListOfPublications) {
            if (lop.getUser().equals(user)
                    && lop.getName().equalsIgnoreCase(normalizedName)
                    && lop.getGenre().equals(genre)) {
                return lop;
            }
        }
        return null;
    }
}