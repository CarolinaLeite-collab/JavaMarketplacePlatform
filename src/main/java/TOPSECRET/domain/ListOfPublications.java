package TOPSECRET.domain;

import java.util.ArrayList;
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
    private List<Publication> _publications;


    public ListOfPublications(User user, String name, Genre genre) {

        if (user == null || name == null || genre == null) {
            throw new IllegalArgumentException("List parameters cannot be null");
        }

        _user = user;
        _name = name;
        _genre = genre;
        _isPrivate = true;
        _publications = new ArrayList<>();
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

    public List<Publication> getPublications() {
        return List.copyOf(_publications);
    }

    public void switchVisibility() {
        _isPrivate = !_isPrivate;
    }

    public void addPublication(Publication publication) {
        if (publication == null) {
            throw new IllegalArgumentException("Publication is mandatory");
        }

        if (_publications.contains(publication)) {
            throw new IllegalStateException("Publication already in list");
        }

        _publications.add(publication);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListOfPublications lop)) return false;
        return Objects.equals(_user, lop.getUser())
                && Objects.equals(_name, lop.getName())
                && Objects.equals(_genre, lop.getGenre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getName(), getGenre());
    }
}
