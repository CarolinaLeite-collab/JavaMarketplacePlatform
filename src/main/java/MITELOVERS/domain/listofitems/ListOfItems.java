package MITELOVERS.domain.listofitems;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a list of items created by a user.
 * <p>
 * Each list has a {@link Name}, a {@link GenreId}, and an associated {@link UserId}.
 * By default, all lists are private. A list can be shared publicly for a defined duration,
 * after which it automatically becomes private again.
 * Two lists are considered equal if they share the same {@link ListOfItemsId}.
 * </p>
 */
public class ListOfItems implements AggregateRoot<ListOfItemsId> {

    private final ListOfItemsId _listOfItemsId;
    private final UserId _userId;
    private final Name _name;
    private final GenreId _genreId;
    private boolean _isPrivate;
    private LocalDateTime _sharedUntil;
    private final List<ItemId> _itemIds;

    /**
     * Creates a new {@link ListOfItems} with a generated {@link ListOfItemsId}.
     * Used by the factory during creation.
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
        _sharedUntil = null;
        _itemIds = new ArrayList<>();
    }

    /**
     * Reconstructs a {@link ListOfItems} from an existing {@link ListOfItemsId}.
     * Used by the assembler during reconstruction from persistence.
     *
     * @throws IllegalArgumentException if any argument is null.
     */
    public ListOfItems(ListOfItemsId listOfItemsId, UserId userId, Name name, GenreId genreId,
                       boolean isPrivate, LocalDateTime sharedUntil) {
        if (listOfItemsId == null) throw new IllegalArgumentException("ListOfItemsId cannot be null");
        if (userId == null) throw new IllegalArgumentException("UserID cannot be null");
        if (name == null) throw new IllegalArgumentException("List name cannot be null");
        if (genreId == null) throw new IllegalArgumentException("GenreID cannot be null");

        _listOfItemsId = listOfItemsId;
        _userId = userId;
        _name = name;
        _genreId = genreId;
        _isPrivate = isPrivate;
        _sharedUntil = sharedUntil;
        _itemIds = new ArrayList<>();
    }

    @Override
    public ListOfItemsId identity() { return _listOfItemsId; }

    public UserId getUserId() { return _userId; }

    public Name getName() { return _name; }

    public GenreId getGenreId() { return _genreId; }

    /**
     * Returns whether this list is private.
     * A list is considered private if it was never shared, or if its sharing duration has expired.
     *
     * @return true if the list is private or the sharing has expired.
     */
    public boolean isPrivate() {
        if (!_isPrivate && _sharedUntil != null && LocalDateTime.now().isAfter(_sharedUntil)) {
            _isPrivate = true;
            _sharedUntil = null;
        }
        return _isPrivate;
    }

    /**
     * Makes this list publicly shared for the given duration.
     *
     * @param duration the duration for which the list will be shared; must not be null.
     * @throws IllegalArgumentException if duration is null.
     */
    public void makePublic(SharedDuration duration) {
        if (duration == null) throw new IllegalArgumentException("Duration cannot be null");
        _isPrivate = false;
        _sharedUntil = LocalDateTime.now().plusDays(duration.getDays());
    }

    public void makePrivate(){
        if (!_isPrivate) {
            _isPrivate = true;
            _sharedUntil = null;
        }
    }

    /**
     * Returns the date and time until which this list is shared publicly.
     *
     * @return the expiry datetime, or null if the list was never shared.
     */
    public LocalDateTime getSharedUntil() { return _sharedUntil; }

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
    public boolean sameAs(Object object) { return equals(object); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListOfItems other)) return false;
        return Objects.equals(_listOfItemsId, other._listOfItemsId);
    }

    @Override
    public int hashCode() { return Objects.hash(_listOfItemsId); }
}