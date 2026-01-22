package TOPSECRET.domain;

import java.util.List;
import java.util.Objects;

/**
 * Represents a list of publications created by a user.
 * <p>
 * Each list has a name, a genre, and an associated user.
 * By default, all lists are private. Two lists are considered equal
 * if they belong to the same user and have the same name and genre.
 * </p>
 */
public class ListOfPublications {
    private User _user;
    private String _name;
    private Genre _genre;
    private boolean _isPrivate;

    public ListOfPublications(User user, String name, Genre genre) {

        if (user == null || name == null || genre == null) {
            throw new IllegalArgumentException("List parameters cannot be null");
        }

        _user = user;
        _name = name;
        _genre = genre;
        _isPrivate = true;
    }


    public User getUser() {
        return _user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListOfPublications lop)) return false;
        return Objects.equals(_user, lop._user) && Objects.equals(_name, lop._name) && Objects.equals(_genre, lop._genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_user, _name, _genre);
    }



}
