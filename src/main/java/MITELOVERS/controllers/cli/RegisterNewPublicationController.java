package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.applicationservices.PublicationService;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import org.springframework.stereotype.Controller;

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

@Controller
public class RegisterNewPublicationController {

    private PublicationService _publicationService;
    private final AuthorService _authorService;
    private final GenreService _genreService;

    public RegisterNewPublicationController(PublicationService publicationService,
                                            AuthorService authorService,
                                            GenreService genreService) {

        _publicationService = Objects.requireNonNull(publicationService, "PublicationService is required");
        _authorService = Objects.requireNonNull(authorService, "AuthorService is required");
        _genreService = Objects.requireNonNull(genreService, "GenreService is required");
    }

    public Iterable<AuthorId> getAuthorsId() {
        return _authorService.getAuthorsId();
    }

    public Iterable<GenreId> getGenresId() {
        return _genreService.getGenresId();
    }

    public Publication registerPublication(Title title,
                                           AuthorId authorId,
                                           Year releaseYear,
                                           GenreId genreId) {

        return _publicationService.registerPublication(
                title,
                authorId,
                releaseYear,
                genreId
        );
    }
}
