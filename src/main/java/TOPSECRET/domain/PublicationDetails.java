package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Identifier;
import TOPSECRET.domain.valueobject.Title;

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
    private final Identifier _identifier;

    public PublicationDetails(Item item) {
        _title = item.get_publication().getTitle();
        _author = item.get_publication().getAuthor();
        _publicationType = item.get_publication().getPublicationType();
        _identifier = item.get_publication().getIdentifier();

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

    public Identifier get_identifier() {
        return _identifier;
    }
}