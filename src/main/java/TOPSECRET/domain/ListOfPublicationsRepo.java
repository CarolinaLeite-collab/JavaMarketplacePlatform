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

    public ListOfPublicationsRepo() {
        _listsOfListOfPublications = new ArrayList<>();
    }

    public ListOfPublications addListOfPublications(ListOfPublications list) {
        if (list == null) {
            throw new IllegalArgumentException("List is mandatory");
        }

        if (existsListOfPublications(list)) {
            return null;
        }

        _listsOfListOfPublications.add(list);
        return list;
    }

    private boolean existsListOfPublications(ListOfPublications listOfPublications) {
        for (ListOfPublications lp1 : _listsOfListOfPublications) {
            if (lp1.equals(listOfPublications)) {
                return true;
            }
        }
        return false;
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



