package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link ListOfItems} instances.
 * <p>
 * This class handles the storage, and retrieval of private lists of publications.
 * It ensures that duplicate lists (same user, name, and genre) are not allowed.
 * </p>
 */

public class MemoListOfItemsRepo implements IListOfItemsRepo {

    private final List<ListOfItems> _listsOfListOfItems;
    private final ListOfItemsFactory _factory;

    public MemoListOfItemsRepo() {
        this(new ListOfItemsFactory());
    }

    public MemoListOfItemsRepo(ListOfItemsFactory factory) {
        _listsOfListOfItems = new ArrayList<>();
        _factory = factory;
    }

    @Override
    public ListOfItems addListOfItems(UserId userId, String name, Genre genre) {
        ListOfItems newList = _factory.createListOfItems(userId, name, genre);
        if (_listsOfListOfItems.contains(newList)) {
            return null;
        }
        _listsOfListOfItems.add(newList);
        return newList;
    }

    @Override
    public List<ListOfItems> getListOfListOfItems() {
        return List.copyOf(_listsOfListOfItems);
    }

    @Override
    public List<ListOfItems> findPublicListsByGenre(Genre genre) {

        List<ListOfItems> result = new ArrayList<>();
        for (ListOfItems lop : _listsOfListOfItems) {
            if (!lop.isPrivate() && lop.getGenre().equals(genre)) {
                result.add(lop);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public List<ListOfItems> findListsByUserId(UserId userId) {

        List<ListOfItems> result = new ArrayList<>();
        for (ListOfItems lop : _listsOfListOfItems) {
            if (lop.getUserId().equals(userId)) {
                result.add(lop);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public ListOfItems findByOwnerNameAndGenre(UserId userId, String name, Genre genre) {

        String normalizedName = name.trim();

        for (ListOfItems lop : _listsOfListOfItems) {
            if (lop.getUserId().equals(userId)
                    && lop.getName().equalsIgnoreCase(normalizedName)
                    && lop.getGenre().equals(genre)) {
                return lop;
            }
        }
        return null;
    }
}
