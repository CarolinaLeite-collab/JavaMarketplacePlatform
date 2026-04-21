package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;
import MITELOVERS.domain.publication.Publication;

import java.time.Year;
import java.util.Objects;

/**
 * Represents the unique technical identifier of a {@link Publication}.
 * <p>
 * A {@code PublicationId} is composed of a {@link Title}, an {@link AuthorId},
 *  * and a release {@link Year}. Two publications are considered the same if they
 *  * share the same title, author, and release year.
 *  * </p>
 *  *
 *  * <p><b>Equality:</b> Two {@code PublicationId} instances are equal if all
 *  * three components are equal.</p>
 *  */


public class PublicationId implements DomainId {

    private final Title _title;
    private final AuthorId _authorId;
    private final Year _releaseYear;

    public PublicationId(Title title, AuthorId authorId, Year releaseYear) {
       _title = title;
       _authorId = authorId;
       _releaseYear =  Objects.requireNonNull(releaseYear, "Release year is required");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationId other)) return false;
        return Objects.equals(_title, other._title)
                && Objects.equals(_authorId, other._authorId)
                && Objects.equals(_releaseYear, other._releaseYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_title, _authorId, _releaseYear);
    }

    @Override
    public String toString() {
        return _title + " - " + _authorId + " (" + _releaseYear + ")";
    }
}

