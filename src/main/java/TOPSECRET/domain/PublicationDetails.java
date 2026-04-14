package TOPSECRET.domain;

import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.PublicationTypeId;
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
    private final AuthorId _authorId;
//    private final PublicationTypeId _publicationTypeId;
    //private final Identifier _identifier;

    public PublicationDetails(Item item) {
        Publication publication = item.get_publication();

        _title = publication.getTitle();
        _authorId = publication.getAuthorId();
//        _publicationTypeId = publication.getPublicationTypeId();
        //_identifier = publication.getIdentifier();
    }

    public Title getTitle() {
        return _title;
    }

    public AuthorId getAuthorId() {
        return _authorId;
    }

//    public PublicationTypeId getPublicationTypeId() {
//        return _publicationTypeId;
//    }

//    public Identifier getIdentifier() {
//        return _identifier;
//    }
}