package TOPSECRET.domain;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.PublicationId;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;

/**
 * Factory responsible for creating {@link Publication} instances.
 * <p>
 * @throws IllegalArgumentException if there's a missing mandatory field null, as enforced by {@link Publication}'s constructor.
 */

public class PublicationFactory {

    public Publication createPublication(Title title, Author author, Year releaseYear, PublicationType publicationType, Genre genre ) {

        return new Publication( title, author, releaseYear, publicationType, genre );
    }

    public Publication createPublication(PublicationId publicationId, Title title, Author author, Year releaseYear, PublicationType publicationType, Genre genre) {

        return new Publication( publicationId, title, author, releaseYear, publicationType, genre );
    }
}
