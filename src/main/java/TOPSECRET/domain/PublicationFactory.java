package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;

/**
 * Factory responsible for creating {@link Publication} instances.
 * <p>
 * @throws IllegalArgumentException if there's a missing mandatory field null, as enforced by {@link Publication}'s constructor.
 */

public class PublicationFactory {

    public Publication createPublication(PublicationType publicationType,
            Identifier identifier,
            Year publicationYear,
            Title title,
            Author author,
            PublishingCompany publisher,
            EditionBook editionBook,
            Genre genre) {

        return Publication.builder()
                .type(publicationType)
                .identifier(identifier)
                .year(publicationYear)
                .title(title)
                .author(author)
                .publisher(publisher)
                .edition(editionBook)
                .genre(genre)
                .build();
    }
}
