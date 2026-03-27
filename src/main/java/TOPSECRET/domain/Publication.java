package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;
import java.util.Objects;

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

public class Publication {

    private final PublicationType _publicationType;
    private final Identifier _identifier;
    private final Year _publicationYear;
    private final Title _title;
    private final Author _author;
    private final PublishingCompany _publisher;
    private final Edition _edition;
    private final Genre _genre;

    private Publication(Builder b) {
        this._publicationType = b._publicationType;
        this._identifier = b._identifier;
        this._publicationYear = b._publicationYear;
        this._title = b._title;
        this._author = b._author;
        this._publisher = b._publisher;
        this._edition = b._edition;
        this._genre = b._genre;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PublicationType _publicationType;
        private Identifier _identifier;
        private Year _publicationYear;
        private Title _title;
        private Author _author;
        private PublishingCompany _publisher;
        private Edition _edition;
        private Genre _genre;


        public Builder type(PublicationType t) { this._publicationType = t; return this; }
        public Builder identifier(Identifier i) { this._identifier = i; return this; }
        public Builder year(Year y) { this._publicationYear = y; return this; }
        public Builder title(Title t) { this._title = t; return this; }
        public Builder author(Author a) { this._author = a; return this; }
        public Builder publisher(PublishingCompany p) { this._publisher = p; return this; }
        public Builder edition(Edition e) { this._edition = e; return this; }
        public Builder genre(Genre g) { this._genre = g; return this; }

        public Publication build() {
            validateCommonFields();
            validateTypeSpecificFields();
            return new Publication(this);
        }

        private void validateCommonFields() {
            require(_publicationType, "publicationType");
            require(_identifier, "identifier");
            require(_publicationYear, "publicationYear");
            require(_title, "title");
        }
        private void validateTypeSpecificFields() {
            String typeName = normalizedType();

            switch (typeName) {
                case "BOOK" -> {
                    require(_author, "author");
                    require(_publisher, "publisher");
                }
                case "MAGAZINE" -> {
                    require(_publisher, "publisher");
                }
            }
        }

        private String normalizedType() {    // THIS METHOD SHOULD BE PUBLICATIONTYPE'S RESPONSABILITY - CHECK LATER
            return _publicationType.getPublicationType().trim().toUpperCase();
        }

        private static <T> void require(T value, String name) {
            if (value == null)
                throw new IllegalArgumentException("Missing mandatory field: " + name);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Publication other)) return false;

        return switch (normalizedType()) {
            case "BOOK" -> _equalsBook(other);
            case "MAGAZINE" -> _equalsMagazine(other);
            default -> _equalsDefault(other);
        };
    }

    private String normalizedType() {
        return _publicationType.getPublicationType().trim().toUpperCase();
    }

    private boolean _equalsBook(Publication other) {
        return (other._publicationYear.getValue() > 1970)
                ? Objects.equals(_identifier, other._identifier)
                : Objects.equals(_title, other._title)
                && Objects.equals(_publicationYear, other._publicationYear);
    }

    private boolean _equalsMagazine(Publication other) {
        return (other._publicationYear.getValue() > 1976)
                ? Objects.equals(_identifier, other._identifier)
                : Objects.equals(_title, other._title)
                && Objects.equals(_publicationYear, other._publicationYear);
    }

    private boolean _equalsDefault(Publication other) {
        return Objects.equals(_title, other._title)
                && Objects.equals(_publicationYear, other._publicationYear);
    }




    public boolean isByAuthor(Author author) {
        return Objects.equals(_author, author);
    }

    public boolean isByGenre(Genre genre) {
        return Objects.equals(_genre, genre);
    }

    public boolean isByPublishingCompany(PublishingCompany publisher) {

        return Objects.equals(_publisher, publisher); }




    public PublicationType getPublicationType() { return _publicationType; }

    public Identifier getIdentifier() { return _identifier; }

    public Year getPublicationYear() { return _publicationYear; }

    public Title getTitle() { return _title; }

    public Author getAuthor() { return _author; }

    public PublishingCompany getPublisher() { return _publisher; }

    public Edition getEdition() { return _edition; }

    public Genre getGenre() { return _genre; }
}
