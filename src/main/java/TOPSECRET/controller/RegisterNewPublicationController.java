package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;
import java.util.Objects;

/**
 * Controller responsible for registering new publications in the system.
 * <p>
 * This controller interacts with the {@link IPublicationRepo} that delegates to {@link PublicationFactory} the creation of
 * new {@link Publication} instances with details such as type, identifier, year,
 * title, author, publisher, edition, and genre. After a new {@link Publication} is instantiated it is stored back in {@link IPublicationRepo}.
 * </p>
 */

public class RegisterNewPublicationController {

    private IPublicationRepo _iPublicationRepo;

    public RegisterNewPublicationController(IPublicationRepo iPublicationRepo, User user) {
        _iPublicationRepo = Objects.requireNonNull(iPublicationRepo, "publicationRepo");
    }

    public Publication registerPublication(Title title, Author author, Year releaseYear, PublicationType publicationType, Genre genre) {

        return _iPublicationRepo.addPublication(title, author, releaseYear, publicationType, genre);
    }
}
