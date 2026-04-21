package MITELOVERS.controller;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.domain.valueobject.UserId;

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

    public RegisterNewPublicationController(IPublicationRepo iPublicationRepo, UserId userId) {
        _iPublicationRepo = Objects.requireNonNull(iPublicationRepo, "publicationRepo");
    }

    public Publication registerPublication(Title title, AuthorId authorId, Year releaseYear, GenreId genreId) {

        return _iPublicationRepo.addPublication(title, authorId, releaseYear,  genreId);
    }
}
