package MITELOVERS.domain.library;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain aggregate root representing a user's Library.
 *
 * <p>
 * A {@code Library} is a core domain entity that groups {@link ItemId} instances
 * belonging to a specific {@link UserId}. Each library is uniquely identified
 * by a {@link LibraryId}, which is derived from the user identity.
 * </p>
 *
 * <p>
 * This aggregate enforces basic invariants such as preventing duplicate item
 * entries and ensuring that only valid item identifiers can be added.
 * It encapsulates all behavior related to managing the collection of items
 * within a user's library.
 * </p>
 */

public class Library implements AggregateRoot<LibraryId> {

    private LibraryId _libraryId;
    private List<ItemId> _itemIds = new ArrayList<>();

    Library(UserId userId) {

        if (userId == null) {
            throw new IllegalArgumentException("LibraryId is required");
        }

        _libraryId = LibraryId.fromUserId(userId);

    }

    @Override
    public LibraryId identity() {

        return _libraryId;

    }

    @Override
    public boolean sameAs(Object object) {

        return equals(object);

    }

    public List<ItemId> getItemsIdInLibrary() {
        return List.copyOf(_itemIds);
    }

    public boolean containsItemId(ItemId itemId) {
        return _itemIds.contains(itemId);
    }

    public boolean addItemIdToLibrary(ItemId itemId) {

        if (itemId == null || _itemIds.contains(itemId)) {
            return false;
        }

        return _itemIds.add(itemId);

    }

    @Override
    public boolean equals(Object object) {

        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Library)) return false;
        Library library = (Library) object;
        return this._libraryId.equals(library._libraryId);

    }

    @Override
    public int hashCode() {
        return _libraryId.hashCode();
    }
}
