package TOPSECRET.domain.library;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.PublicationDetails;
import TOPSECRET.domain.User;
import TOPSECRET.domain.valueobject.LibraryId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a {@link User} library in the domain model.
 *
 * <p>
 * A {@code Library} is an entity that groups publications and is owned by
 * a {@link User}. A library is uniquely identified within the system
 * and encapsulates the core data related to a user's library.
 *
 */

public class Library implements AggregateRoot<LibraryId> {

    private LibraryId _libraryId;
    private List<Item> _items = new ArrayList<>();

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

    public List<PublicationDetails> getItemDetails() {

        List <PublicationDetails> listWithDetails = new ArrayList<>();

        for (Item p : _items) {

            PublicationDetails pDetails = new PublicationDetails(p);

            listWithDetails.add(pDetails);
        }

        return Collections.unmodifiableList(listWithDetails);
    }

    public List<Item> getItemsInLibrary() {
        return List.copyOf(_items);
    }

    public boolean addItemToLibrary(Item item) {
        if (item == null) {
            return false;
        }

        if (_items.contains(item)) {
            return false;
        }

        _items.add(item);
        return true;
    }

    public Item getItem(Item item) {
        for (Item item1 : _items) {
            if (item.equals(item1)) {
                return item1;
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
