package TOPSECRET.domain;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.PublicationId;
import TOPSECRET.domain.valueobject.Title;

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
    private final PublicationTypeId _publicationTypeId;
    private final GenreId _genreId;

    // Creation
    protected Publication( Title title, AuthorId authorId, Year releaseYear, PublicationTypeId publicationTypeId, GenreId genreId ){
        _title = title;
        _authorId = authorId;
        _releaseYear = Objects.requireNonNull(releaseYear, "Release year is required");
        _publicationTypeId = publicationTypeId;
        _genreId = genreId;
        _publicationId = new PublicationId(_title, _authorId, _releaseYear);
    }

    // Reconstitution
    protected Publication(PublicationId publicationId, Title title, AuthorId authorId, Year releaseYear, PublicationTypeId publicationTypeId, GenreId genreId) {
        _publicationId = Objects.requireNonNull(publicationId, "PublicationId is required");
        _title = title;
        _authorId = authorId;
        _releaseYear = Objects.requireNonNull(releaseYear, "Release year is required");
        _publicationTypeId = publicationTypeId;
        _genreId = genreId;
    }

    public boolean isByAuthor(AuthorId authorId) {
        return Objects.equals(_authorId, authorId);
    }

    public boolean isByGenre(GenreId genreId) {
        return Objects.equals(_genreId, genreId);
    }

    public PublicationId getPublicationId() { return _publicationId; }
    public Title getTitle() { return _title; }
    public AuthorId getAuthorId() { return _authorId; }
    public Year getReleaseYear() { return _releaseYear; }
    public PublicationTypeId getPublicationTypeId() { return _publicationTypeId; }
    public GenreId getGenreId() { return _genreId; }

    @Override
    public PublicationId identity() { return _publicationId; }

    @Override
    public boolean sameAs(Object object) {
        if (!(object instanceof Publication other)) return false;
        return _publicationId.equals(other._publicationId);
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
}
