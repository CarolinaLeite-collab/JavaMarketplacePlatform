package MITELOVERS.domain.publication;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import org.springframework.stereotype.Component;

import java.time.Year;

/**
 * Factory responsible for creating {@link Publication} instances.
 * <p>
 * @throws IllegalArgumentException if there's a missing mandatory field null, as enforced by {@link Publication}'s constructor.
 */

@Component
public class PublicationFactory {

    public Publication createPublication(Title title, AuthorId authorId, Year releaseYear, GenreId genreId ) {

        return new Publication( title, authorId, releaseYear, genreId );
    }

    public Publication createPublication(PublicationId publicationId, Title title, AuthorId authorId,  Year releaseYear, GenreId genreId ) {

        return new Publication(publicationId, title, authorId, releaseYear, genreId);
    }

}
