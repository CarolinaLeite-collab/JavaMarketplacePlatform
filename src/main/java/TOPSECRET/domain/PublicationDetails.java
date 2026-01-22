package TOPSECRET.domain;

/**
 * Represents a collection of data retrieved from {@link Publication}.
 * <p>
 * Includes the title, author, publication type, and identifier of the publication.
 * This class is used to display publication information without exposing full domain objects.
 * </p>
 */

public class PublicationDetails {

    private final Title _title;
    private final Author _author;
    private final PublicationType _publicationType;
    private final Identifier identifier;

    public PublicationDetails(Publication publication) {
        _title = publication.getTitle();
        _author = publication.getAuthor();
        _publicationType = publication.getPublicationType();
        identifier = publication.getIdentifier();

    }
    public Title getTitle() {
        return _title;
    }

    public Author getAuthor() {
        return _author;
    }

    public PublicationType getPublicationType() {
        return _publicationType;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}