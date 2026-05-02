package MITELOVERS.domain.author;


import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;

import java.util.Objects;

/**
 * Author is the person who originates, creates, and writes a `publication`.
 * The same name can mean different authors.
 */

public class Author implements AggregateRoot<AuthorId> {

    private final Name _name;
    private final AuthorId _authorId;


    Author (AuthorId authorId, Name name) {

        _name = Objects.requireNonNull(name, "Name cannot be null");
        _authorId = authorId;
    }

    Author (Name name) {
        this(new AuthorId(name), name);
    }


    @Override
    public AuthorId identity() {

        return _authorId;

    }

    @Override
    public boolean sameAs(Object object) {

        if (object instanceof Author other) {

            return _name.equals(other._name);

        }

        return false;
    }

    public Name getName() {

        return _name;

    }

    @Override
    public boolean equals(Object object) {

        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Author)) return false;
        Author author = (Author) object;
        return _authorId.equals(author._authorId);

    }

    @Override
    public int hashCode() {

        return _authorId.hashCode();

    }

}
