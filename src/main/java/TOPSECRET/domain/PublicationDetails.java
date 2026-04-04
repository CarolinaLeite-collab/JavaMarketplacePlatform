package TOPSECRET.domain;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.valueobject.Title;

/**
 * Represents a collection of data retrieved from {@link Item}.
 * <p>
 * Includes the title, author, publication type, and identifier of the publication within an item.
 * This class is used to display item information without exposing full domain objects.
 * </p>
 */

public class PublicationDetails {

    private final Title _title;
    private final Author _author;
    private final PublicationType _publicationType;
    //private final Identifier _identifier;

    public PublicationDetails(Item item) {
        Publication publication = item.get_publication();

        _title = publication.getTitle();
        _author = publication.getAuthor();
        _publicationType = publication.getPublicationType();
        //_identifier = publication.getIdentifier();
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

//    public Identifier getIdentifier() {
//        return _identifier;
//    }
}