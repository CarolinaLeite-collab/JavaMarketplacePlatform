package TOPSECRET.domain;

import TOPSECRET.domain.ListOfItems.ListOfItems;
import TOPSECRET.domain.ListOfItems.ListOfItemsFactory;
import TOPSECRET.domain.valueobject.GenreId;
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

    private final List<ListOfItems> _lists;
    private final ListOfItemsFactory _factory;

    public MemoListOfItemsRepo() {
        this(new ListOfItemsFactory());
    }

    public MemoListOfItemsRepo(ListOfItemsFactory factory) {
        _lists = new ArrayList<>();
        _factory = factory;
    }

    @Override
    public ListOfItems addListOfItems(UserId userId, String name, GenreId genreId) {
        ListOfItems newList = _factory.createListOfItems(userId, name, genreId);
        boolean exists = _lists.stream()
                .anyMatch(existing -> existing.identity().equals(newList.identity()));

        if (exists) return null;
        _lists.add(newList);
        return newList;
    }

    @Override
    public List<ListOfItems> getListOfListOfItems() {
        return List.copyOf(_lists);
    }

    @Override
    public List<ListOfItems> findPublicListsByGenre(GenreId genreId) {

        List<ListOfItems> result = new ArrayList<>();
        for (ListOfItems lop : _lists) {
            if (!lop.isPrivate() && lop.getGenreId().equals(genreId)) {
                result.add(lop);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public List<ListOfItems> findListsByUserId(UserId userId) {

        List<ListOfItems> result = new ArrayList<>();
        for (ListOfItems lop : _lists) {
            if (lop.getUserId().equals(userId)) {
                result.add(lop);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public ListOfItems findByOwnerNameAndGenre(UserId userId, String name, GenreId genreId) {

        String normalizedName = name.trim();

        for (ListOfItems lop : _lists) {
            if (lop.getUserId().equals(userId)
                    && lop.getName().equalsIgnoreCase(normalizedName)
                    && lop.getGenreId().equals(genreId)) {
                return lop;
            }
        }
        return null;
    }
}
