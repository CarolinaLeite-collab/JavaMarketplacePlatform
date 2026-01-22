package TOPSECRET.domain;

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
    private final Publisher _publisher;
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
        private Publisher _publisher;
        private Edition _edition;
        private Genre _genre;

        public Builder type(PublicationType t) {
            this._publicationType = t;
            return this;
        }

        public Builder identifier(Identifier i) {
            this._identifier = i;
            return this;
        }

        public Builder year(Year y) {
            this._publicationYear = y;
            return this;
        }

        public Builder title(Title t) {
            this._title = t;
            return this;
        }

        public Builder author(Author a) {
            this._author = a;
            return this;
        }

        public Builder publisher(Publisher p) {
            this._publisher = p;
            return this;
        }

        public Builder edition(Edition e) {
            this._edition = e;
            return this;
        }

        public Builder genre(Genre g) {
            this._genre = g;
            return this;
        }

        public Publication build() {
            require(_publicationType, "publicationType");
            require(_identifier, "identifier");
            require(_publicationYear, "publicationYear");
            require(_title, "title");

            // Mandatory fields that vary with publication type
            String typeName = _publicationType.getPublicationType().trim().toUpperCase();
            switch (typeName) {
                case "BOOK" -> {
                    require(_author, "author");
                    require(_publisher, "publisher");
                }
                case "MAGAZINE" -> {
                    require(_publisher, "publisher");
                }
                default -> {
                    // no extra mandatory fields
                }
            }

            return new Publication(this);
        }

        private static <T> void require(T v, String n) {
            if (v == null) throw new IllegalArgumentException("Missing mandatory field: " + n);
        }
    }


    //method to see if publication is the same as other
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Publication other = (Publication) obj;

        String sanitizedPublicationType = _publicationType.getPublicationType().trim().toUpperCase();
        switch (sanitizedPublicationType) {
            case "BOOK" -> {
                if (other._publicationYear.getValue() > 1970) {
                    return Objects.equals(this._identifier, other._identifier);
                } else {
                    return Objects.equals(other._title, this._title)
                            && Objects.equals(this._publicationYear, other._publicationYear);
                }
            }
            case "MAGAZINE" -> {
                if (other._publicationYear.getValue() > 1976) {
                    return Objects.equals(this._identifier, other._identifier);
                } else {
                    return Objects.equals(this._title, other._title)
                            && Objects.equals(this._publicationYear, other._publicationYear);
                }
            }
            default -> {
                return Objects.equals(this._title, other._title)
                        && Objects.equals(this._publicationYear, other._publicationYear);
            }
        }
    }

    //Method to get items' genre that are on direct sale

    public boolean matchGenre(Genre genre) {
        if (genre == null) {
            return false;
        }
        return genre.equals(this._genre);
    }

    //getters
    public PublicationType getPublicationType() {
        return _publicationType;
    }

    public Identifier getIdentifier() {
        return _identifier;
    }

    public Year getPublicationYear() {
        return _publicationYear;
    }

    public Title getTitle() {
        return _title;
    }

    public Author getAuthor() {
        return _author;
    }

    public Publisher getPublisher() {
        return _publisher;
    }

    public Edition getEdition() {
        return _edition;
    }

    public Genre getGenre() {
        return _genre;
    }


}
