package TOPSECRET.domain.ListOfItems;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.ListOfItemsId;
import TOPSECRET.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a list of items created by a user.
 * <p>
 * Each list has a name, a genre, and an associated user.
 * By default, all lists are private. Two lists are considered equal
 * if they belong to the same user and have the same name and genre.
 * </p>
 */

public class ListOfItems implements AggregateRoot<ListOfItemsId> {

    private final ListOfItemsId _listOfItemsId;
    private UserId _userId;
    private String _name;
    private GenreId _genreId;
    private boolean _isPrivate;
    private List<ItemId> _itemIds;

    ListOfItems(ListOfItemsId listOfItemsId, UserId userId, String name, GenreId genreId) {

        if (listOfItemsId == null) {
            throw new IllegalArgumentException("ListOfItemsID cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserID cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("List name cannot be null");
        }
        if (genreId == null) {
            throw new IllegalArgumentException("GenreID cannot be null");
        }

        _listOfItemsId = listOfItemsId;
        _userId = userId;
        _name = name;
        _genreId = genreId;
        _isPrivate = true;
        _itemIds = new ArrayList<>();
    }

    @Override
    public ListOfItemsId identity() {
        return _listOfItemsId;
    }

    public UserId getUserId() {
        return _userId;
    }

    public String getName() {
        return _name;
    }

    public GenreId getGenreId() {
        return _genreId;
    }

    public boolean isPrivate() {
        return _isPrivate;
    }

    public void makePublic() {
        _isPrivate = false;
    }

    public List<ItemId> getItemIds() {
        return List.copyOf(_itemIds);
    }


    public void addItem(ItemId itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item is mandatory");
        }

        if (_itemIds.contains(itemId)) {
            throw new IllegalStateException("Item already in list");
        }

        _itemIds.add(itemId);
    }

    @Override
    public boolean sameAs(Object object) {
        if (this == object) return true;
        if (!(object instanceof ListOfItems other)) return false;
        return Objects.equals(_listOfItemsId, other._listOfItemsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_listOfItemsId);
    }
}

