package TOPSECRET.domain;

import TOPSECRET.domain.User.User;

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

    public List<PublicationDetails> getPublicationDetails() {

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
}
