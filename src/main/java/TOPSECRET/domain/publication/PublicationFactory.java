package TOPSECRET.domain.publication;

import TOPSECRET.domain.valueobject.*;

import java.time.Year;

/**
 * Factory responsible for creating {@link Publication} instances.
 * <p>
 * @throws IllegalArgumentException if there's a missing mandatory field null, as enforced by {@link Publication}'s constructor.
 */

public class PublicationFactory {

    public Publication createPublication(Title title, AuthorId authorId, Year releaseYear, GenreId genreId ) {

        return new Publication( title, authorId, releaseYear, genreId );
    }

}
