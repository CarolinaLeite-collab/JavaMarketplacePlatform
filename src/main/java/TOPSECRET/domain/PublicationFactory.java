package TOPSECRET.domain;

/**
 * Factory responsible for creating {@link Publication} instances.
 * <p>
 * @throws IllegalArgumentException if there's a missing mandatory field null, as enforced by {@link Publication}'s constructor.
 */

import java.time.Year;


public class PublicationFactory {

    public Publication createPublication(

            PublicationType type,
            Identifier identifier,
            Year year,
            Title title,
            Author author,
            PublishingCompany publisher,
            Edition edition,
            Genre genre
    ) {
        return Publication.builder()
                .type(type)
                .identifier(identifier)
                .year(year)
                .title(title)
                .author(author)
                .publisher(publisher)
                .edition(edition)
                .genre(genre)
                .build();
    }
}
