package TOPSECRET.domain;


import java.util.Objects;

/**
 *Catalog-level metadata describing a publication independently of any physical copy.
 */

public class PublicationInfo {

    private final Title _title;
    private final Genre _genre;
    private final Author _author;
    private final Edition _edition;
    private final Publisher _publisher;


    public PublicationInfo ( Title title, Genre genre, Author author, Edition edition, Publisher publisher){

        _title = Objects.requireNonNull(title, "title is required");
        _genre = Objects.requireNonNull(genre, "genre is required");
        _author = Objects.requireNonNull(author, "author is required");
        _edition = Objects.requireNonNull(edition, "edition is required");
        _publisher = Objects.requireNonNull(publisher, "publisher is required");

    }

    public Title getTitle() {
        return _title;
    }
    public Genre getGenre() {
        return _genre;
    }

    public Author getAuthor() {
        return _author;
    }

    public Edition getEdition() {
        return _edition;
    }

    public Publisher getPublisher() {
        return _publisher;
    }

    public ISBN getISBN() {
        return _edition.getIsbn();
    }

    public ISSN getISSN() {
        return _edition.getIssn();
    }
}
