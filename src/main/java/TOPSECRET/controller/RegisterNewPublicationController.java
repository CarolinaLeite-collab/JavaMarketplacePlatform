package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.time.Year;
import java.util.Objects;

/**
 * Controller responsible for registering new publications in the system.
 * <p>
 * This controller interacts with the {@link PublicationRepo} to create and store
 * new {@link Publication} instances with details such as type, identifier, year,
 * title, author, publisher, edition, and genre.
 * </p>
 */

public class RegisterNewPublicationController {
    private PublicationRepo _publicationRepo;

    public RegisterNewPublicationController(PublicationRepo publicationRepo) {
        this._publicationRepo = Objects.requireNonNull(publicationRepo, "publicationRepo");    }

    public Publication registerPublication(PublicationType publicationType,
                                           Identifier identifier,
                                           Year publicationYear,
                                           Title title,
                                           Author author,
                                           Publisher publisher,
                                           Edition edition,
                                           Genre genre) {

        Publication myPublication = Publication.builder()
                .type(publicationType)
                .identifier(identifier)
                .year(publicationYear)
                .title(title)
                .author(author)
                .publisher(publisher)
                .edition(edition)
                .genre(genre)
                .build();

        return _publicationRepo.add(myPublication);
    }
}
