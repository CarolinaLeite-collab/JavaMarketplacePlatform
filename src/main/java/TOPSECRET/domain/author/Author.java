package TOPSECRET.domain.author;


import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.AuthorId;

/**
 * Author is the person who originates, creates, and writes a literary work.
 * The same name can mean different authors.
 */

public class Author implements AggregateRoot<AuthorId> {

    private final String _name;
    private final AuthorId _authorId;


    Author (String name) {

        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Author name cannot be null or empty");

        _name = name;
        _authorId = new AuthorId(name);

    }


    @Override
    public AuthorId identity() {

        return _authorId;

    }

    @Override
    public boolean sameAs(Object object) {

        return equals(object);

    }

    public String getName() {

        return _name;

    }

    public String getLowerCaseName() {

        return _name.toLowerCase();

    }

    @Override
    public boolean equals(Object object) {

        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Author)) return false;
        Author author = (Author) object;
        return this._authorId.equals(author._authorId);

    }

    @Override
    public int hashCode() {

        return _name.toLowerCase().hashCode();

    }

}
