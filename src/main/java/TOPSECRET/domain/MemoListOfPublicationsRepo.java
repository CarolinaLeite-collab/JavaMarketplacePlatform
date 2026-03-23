package TOPSECRET.domain;

import TOPSECRET.ddd.IListOfPublicationsRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link ListOfPublications} instances.
 * <p>
 * This class handles the storage, and retrieval of private lists of publications.
 * It ensures that duplicate lists (same user, name, and genre) are not allowed.
 * </p>
 */

public class MemoListOfPublicationsRepo implements IListOfPublicationsRepo {
    private final List<ListOfPublications> _listsOfListOfPublications;
    private final ListOfPublicationsFactory _factory;

    public MemoListOfPublicationsRepo() {
        this(new ListOfPublicationsFactory());
    }

    public MemoListOfPublicationsRepo(ListOfPublicationsFactory factory) {
        _listsOfListOfPublications = new ArrayList<>();
        _factory = factory;
    }

    @Override
    public ListOfPublications addListOfPublications(User user, String name, Genre genre) {
        ListOfPublications newList = _factory.createListOfPublications(user, name, genre);
        if (_listsOfListOfPublications.contains(newList)) {
            return null;
        }
        _listsOfListOfPublications.add(newList);
        return newList;
    }

    @Override
    public List<ListOfPublications> getListOfListOfPublications() {
        return List.copyOf(_listsOfListOfPublications);
    }

    @Override
    public List<ListOfPublications> findPublicListsByGenre(Genre genre) {

        List<ListOfPublications> result = new ArrayList<>();
        for (ListOfPublications lop : _listsOfListOfPublications) {
            if (!lop.isPrivate() && lop.getGenre().equals(genre)) {
                result.add(lop);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public List<ListOfPublications> findListsByUser(User user) {

        List<ListOfPublications> result = new ArrayList<>();
        for (ListOfPublications lop : _listsOfListOfPublications) {
            if (lop.getUser().equals(user)) {
                result.add(lop);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public ListOfPublications findByOwnerNameAndGenre(User user, String name, Genre genre) {

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
