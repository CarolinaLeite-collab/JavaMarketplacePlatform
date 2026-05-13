package MITELOVERS.controller;

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

    private IPublicationRepo _iPublicationRepo;
    private PublicationFactory _publicationFactory;
    private IAuthorRepo _iAuthorRepo;
    private IGenreRepo _iGenreRepo;

    public RegisterNewPublicationController(IPublicationRepo iPublicationRepo, PublicationFactory publicationFactory, IAuthorRepo iAuthorRepo, IGenreRepo iGenreRepo) {
        _iPublicationRepo = Objects.requireNonNull(iPublicationRepo, "publicationRepo is required");
        _publicationFactory = Objects.requireNonNull(publicationFactory, "publicationFactory is required");
        _iAuthorRepo =  Objects.requireNonNull(iAuthorRepo, "authorRepo is required");
        _iGenreRepo = Objects.requireNonNull(iGenreRepo, "genreRepo is required");
    }

    public Iterable<AuthorId> getAuthorsId(){

        return _iAuthorRepo.findAllKeys();
    }

    public Iterable <GenreId> getGenresId(){

        return _iGenreRepo.findAllKeys();
    }


    public Publication registerPublication(Title title, AuthorId authorId, Year releaseYear, GenreId genreId) {

        Publication newPublication = _publicationFactory.createPublication(title, authorId, releaseYear, genreId);

        if (_iPublicationRepo.containsOfIdentity(newPublication.identity())){
            throw new IllegalArgumentException("Publication already exists in the repository");
        }
        return _iPublicationRepo.save(newPublication);
    }
}
