package TOPSECRET.domain;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.PublicationId;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a publication in the MiteLovers domain.
 * <p>
 * A {@code Publication} is an Aggregate Root that captures the immutable attributes
 * of a publication: its {@link Title}, {@link Author}, release {@link Year},
 * {@link PublicationType}, and {@link Genre}.
 * </p>
 *
 * <p><b>Identity:</b> Each {@code Publication} is assigned a unique {@link PublicationId}
 * generated at creation time. Two publications are considered equal if they share
 * the same {@link Title}, {@link Author}, and release {@link Year}, regardless of
 * their {@link PublicationId}.</p>
 */

public class Publication implements AggregateRoot<PublicationId> {

    private final PublicationId _publicationId;
    private final Title _title;
    private final Author _author;
    private final Year _releaseYear;
    private final PublicationType _publicationType;
    private final Genre _genre;

    // Creation
    protected Publication( Title title, Author author, Year releaseYear, PublicationType publicationType, Genre genre ){
        _title = title;
        _author = author;
        _releaseYear = Objects.requireNonNull(releaseYear, "Release year is required");
        _publicationType = publicationType;
        _genre = genre;
        _publicationId = new PublicationId(UUID.randomUUID().toString());
    }

    // Reconstitution
    protected Publication(PublicationId publicationId, Title title, Author author, Year releaseYear,
                          PublicationType publicationType, Genre genre) {
        _publicationId = Objects.requireNonNull(publicationId, "PublicationId is required");
        _title = title;
        _author = author;
        _releaseYear = Objects.requireNonNull(releaseYear, "Release year is required");
        _publicationType = publicationType;
        _genre = genre;
    }

    public boolean isByAuthor(Author author) {
        return Objects.equals(_author, author);
    }

    public boolean isByGenre(Genre genre) {
        return Objects.equals(_genre, genre);
    }

    public PublicationId getPublicationId() { return _publicationId; }
    public Title getTitle() { return _title; }
    public Author getAuthor() { return _author; }
    public Year getReleaseYear() { return _releaseYear; }
    public PublicationType getPublicationType() { return _publicationType; }
    public Genre getGenre() { return _genre; }

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
        return Objects.equals(_title, other._title)
                && Objects.equals(_author, other._author)
                && Objects.equals(_releaseYear, other._releaseYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_title, _author, _releaseYear);
    }
}
