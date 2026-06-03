package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.applicationservices.PublicationService;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import org.springframework.stereotype.Controller;

import java.time.Year;
import java.util.Objects;

/**
 * Controller responsible for registering new publications in the system.
 * <p>
 * This controller coordinates publication registration through the
 * {@link PublicationService}. It also provides access to available
 * {@link AuthorId} and {@link GenreId} values through the
 * {@link AuthorService} and {@link GenreService}, respectively.
 * </p>
 * <p>
 * When a new publication is registered, the request is delegated to the
 * application service layer, which validates the provided information,
 * creates the corresponding {@link Publication} and persists it.
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
