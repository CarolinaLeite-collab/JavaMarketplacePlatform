package MITELOVERS.domain.publication;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.*;

import java.time.Year;
import java.util.Objects;

/**
 * Represents a publication in the MiteLovers domain.
 * <p>
 * A {@code Publication} is an Aggregate Root that captures the immutable attributes
 *  * of a publication: its {@link Title}, {@link AuthorId}, release {@link Year},
 *  * {@link PublicationTypeId}, and {@link GenreId}.
 *  * </p
 *
 * <p><b>Identity:</b> Each {@code Publication} is identified by a {@link PublicationId}
 *  * composed of its {@link Title}, {@link AuthorId}, and release {@link Year}.
 *  * Two publications are considered equal if they share the same title, author, and release year.</p>
 */

public class Publication implements AggregateRoot<PublicationId> {

    private final PublicationId _publicationId;
    private final Title _title;
    private final AuthorId _authorId;
    private final Year _releaseYear;
    private final GenreId _genreId;

    Publication(PublicationId publicationId, Title title, AuthorId authorId, Year releaseYear, GenreId genreId ){
        _title = Objects.requireNonNull(title, "Title is required");
        _authorId = Objects.requireNonNull(authorId, "AuthorId is required");
        _releaseYear = Objects.requireNonNull(releaseYear, "Release year is required");
        _genreId = Objects.requireNonNull(genreId, "GenreId is required");
        _publicationId  = Objects.requireNonNull(publicationId, "PublicationId is required");
    }

    Publication(Title title, AuthorId authorId, Year releaseYear, GenreId genreId ) {
        this(new PublicationId(title, authorId, releaseYear), title, authorId, releaseYear, genreId);
    }

    public boolean isByAuthorId(AuthorId authorId) {
        return Objects.equals(_authorId, authorId);
    }

    public boolean isByGenreId(GenreId genreId) {
        return Objects.equals(_genreId, genreId);
    }

    public Title getTitle() { return _title; }
    public AuthorId getAuthorId() { return _authorId; }
    public Year getReleaseYear() { return _releaseYear; }
    public GenreId getGenreId() { return _genreId; }

    @Override
    public PublicationId identity() { return _publicationId; }

    @Override
    public boolean sameAs(Object object) {
        if (!(object instanceof Publication other)) return false;
        return _title.equals(other._title)
                && _authorId.equals(other._authorId)
                && _releaseYear.equals(other._releaseYear)
                && _genreId.equals(other._genreId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication other)) return false;
        return _publicationId.equals(other._publicationId);
    }

    @Override
    public int hashCode() {
        return _publicationId.hashCode();
    }

    @Override
    public String toString() {
        return  "Id: " + _publicationId.toString() +
                "\nTitle: " + _title.toString() +
                "\nAuthor: " + _authorId.toString() +
                "\nRelease Year: " + _releaseYear.toString() +
                "\nGenre: " + _genreId.toString();

    }
}
