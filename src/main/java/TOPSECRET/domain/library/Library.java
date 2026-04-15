package TOPSECRET.domain.library;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.LibraryId;
import TOPSECRET.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@link UserId} library in the domain model.
 *
 * <p>
 * A {@code Library} is an entity that groups publications and is owned by
 * a {@link UserId}. A library is uniquely identified within the system
 * and encapsulates the core data related to a user's library.
 *
 */

public class Library implements AggregateRoot<LibraryId> {

    private LibraryId _libraryId;
    private List<ItemId> _itemsId = new ArrayList<>();

    Library(LibraryId libraryId){

        if (libraryId == null) {
            throw new IllegalArgumentException("LibraryId is required");
        }

        _libraryId = libraryId;

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
        return List.copyOf(_itemsId);
    }

    public boolean addItemIdToLibrary(ItemId itemId) {
        if (itemId == null) {
            return false;
        }

        if (_itemsId.contains(itemId)) {
            return false;
        }

        _itemsId.add(itemId);
        return true;
    }

    public ItemId getItemId(ItemId itemId) {
        for (ItemId id : _itemsId) {
            if (itemId.equals(id)) {
                return id;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object object) {

        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Library)) return false;
        Library library = (Library) object;
        return this._libraryId.equals(library._libraryId);

    }
}
