package TOPSECRET.domain;

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

public class Library {

    private User _owner;

    private List<Publication> _publications = new ArrayList<>();
    private List<Item> _items = new ArrayList<>();

    Library(User user){

        if (user == null)
            throw new IllegalArgumentException("User is required");

        _owner = user;

    }

    public boolean belongsTo(User user){
        return _owner.equals(user);
    }

    public User getUser() {
        return _owner;
    }

    /**
     * Manages the items stored in the library.
     *
     * <p>This set of operations allows retrieving, adding, and searching for
     * {@link Item} objects maintained by the library.</p>
     *
     * <ul>
     *     <li>{@link #getItemsInLibrary()} returns an <strong>unmodifiable list</strong>
     *     containing all items currently stored in the library.</li>
     *     <li>{@link #addItemToLibrary(Item)} adds a new item to the library if it
     *     is not {@code null} and does not already exist.</li>
     *     <li>{@link #getItem(Item)} searches for and returns the matching item
     *     stored in the library using {@link Object#equals(Object)}, or
     *     {@code null} if no match is found.</li>
     * </ul>
     *
     * <p>The returned collections are immutable to preserve encapsulation
     * and prevent external modification of the library's internal state.</p>
     */


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
}
