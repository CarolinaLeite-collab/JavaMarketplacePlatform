package MITELOVERS.domain.listofitems;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a list of items created by a user.
 * <p>
 * Each list has a {@link Name}, a {@link GenreId}, and an associated {@link UserId}.
 * By default, all lists are private. Two lists are considered equal if they share
 * the same {@link ListOfItemsId}.
 * </p>
 */
public class ListOfItems implements AggregateRoot<ListOfItemsId> {

    private final ListOfItemsId _listOfItemsId;
    private final UserId _userId;
    private final Name _name;
    private final GenreId _genreId;
    private boolean _isPrivate;
    private final List<ItemId> _itemIds;

    /**
     * Creates a new {@link ListOfItems} with a generated {@link ListOfItemsId}.
     * Used by the controller during creation.
     *
     * @throws IllegalArgumentException if any argument is null.
     */
    public ListOfItems(UserId userId, Name name, GenreId genreId) {
        if (userId == null) throw new IllegalArgumentException("UserID cannot be null");
        if (name == null) throw new IllegalArgumentException("List name cannot be null");
        if (genreId == null) throw new IllegalArgumentException("GenreID cannot be null");

        _listOfItemsId = ListOfItemsId.newId();
        _userId = userId;
        _name = name;
        _genreId = genreId;
        _isPrivate = true;
        _itemIds = new ArrayList<>();
    }

    /**
     * Reconstructs a {@link ListOfItems} from an existing {@link ListOfItemsId}.
     * Used by the assembler during reconstruction from persistence.
     *
     * @throws IllegalArgumentException if any argument is null.
     */
    public ListOfItems(ListOfItemsId listOfItemsId, UserId userId, Name name, GenreId genreId) {
        if (listOfItemsId == null) throw new IllegalArgumentException("ListOfItemsId cannot be null");
        if (userId == null) throw new IllegalArgumentException("UserID cannot be null");
        if (name == null) throw new IllegalArgumentException("List name cannot be null");
        if (genreId == null) throw new IllegalArgumentException("GenreID cannot be null");

        _listOfItemsId = listOfItemsId;
        _userId = userId;
        _name = name;
        _genreId = genreId;
        _isPrivate = true;
        _itemIds = new ArrayList<>();
    }

    @Override
    public ListOfItemsId identity() { return _listOfItemsId; }

    public UserId getUserId() { return _userId; }

    public Name getName() { return _name; }

    public GenreId getGenreId() { return _genreId; }

    public boolean isPrivate() { return _isPrivate; }

    public void makePublic() { _isPrivate = false; }

    public List<ItemId> getItemIds() { return List.copyOf(_itemIds); }

    /**
     * Adds an {@link ItemId} to this list.
     *
     * @throws IllegalArgumentException if itemId is null.
     * @throws IllegalStateException    if the item is already in the list.
     */
    public void addItem(ItemId itemId) {
        if (itemId == null) throw new IllegalArgumentException("Item is mandatory");
        if (_itemIds.contains(itemId)) throw new IllegalStateException("Item already in list");
        _itemIds.add(itemId);
    }

    @Override
    public boolean sameAs(Object object) {
        return equals(object);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListOfItems other)) return false;
        return Objects.equals(_listOfItemsId, other._listOfItemsId);
    }

    @Override
    public int hashCode() { return Objects.hash(_listOfItemsId); }

}