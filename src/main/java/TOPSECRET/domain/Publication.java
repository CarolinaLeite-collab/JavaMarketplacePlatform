package TOPSECRET.domain;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.PublicationId;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents catalog-level metadata for a publication.
 * <p>
 * Contains information such as {@link PublicationType}, {@link Identifier}, publication year,
 * title, author, publisher, edition, and genre.
 * <p>
 * Uses the Builder pattern to construct instances, enforcing mandatory fields depending on the publication type
 * (e.g., books require author and publisher, magazines require publisher).
 * </p>
 * <p>
 * Equality of publications is determined based on identifier(ISBN or ISSN), title, and publication year,
 * with specific rules for books and magazines depending on the publication year.
 * </p>
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
