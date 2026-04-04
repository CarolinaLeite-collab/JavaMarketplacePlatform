package TOPSECRET.domain;

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
public class ListOfItems {

    private UserId _userId;
    private String _name;
    private Genre _genre;
    private boolean _isPrivate;
    private List<Item> _items;


    ListOfItems(UserId userId, String name, Genre genre) {

        if (name == null) {
            throw new IllegalArgumentException("List name cannot be null");
        }

        _userId = userId;
        _name = name;
        _genre = genre;
        _isPrivate = true;
        _items = new ArrayList<>();
    }

    public UserId getUserId() {
        return _userId;
    }

    public String getName() {
        return _name;
    }

    public Genre getGenre() {
        return _genre;
    }

    public boolean isPrivate() {
        return _isPrivate;
    }

    public void makePublic() {
        _isPrivate = false;
    }

    public List<Item> getItems() {
        return List.copyOf(_items);
    }


    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is mandatory");
        }

        if (_items.contains(item)) {
            throw new IllegalStateException("Item already in list");
        }

        _items.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListOfItems lop)) return false;
        return Objects.equals(_userId, lop.getUserId())
                && Objects.equals(_name, lop.getName())
                && Objects.equals(_genre, lop.getGenre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getName(), getGenre());
    }

}
