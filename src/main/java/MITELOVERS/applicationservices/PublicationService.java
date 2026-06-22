package MITELOVERS.applicationservices;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Application service responsible for retrieving publication information
 * and converting domain objects into response DTOs.
 */

@Service
public class PublicationService {

    private final IPublicationRepo _iPublicationRepo;
    private final PublicationFactory _publicationFactory;
    private final IGenreRepo _iGenreRepo;
    private final IAuthorRepo _iAuthorRepo;

    public PublicationService(IPublicationRepo iPublicationRepo,
                              PublicationFactory publicationFactory,
                              IGenreRepo iGenreRepo,
                              IAuthorRepo iAuthorRepo
    ) {

        _iPublicationRepo = Objects.requireNonNull(iPublicationRepo, "PublicationRepo is required");
        _publicationFactory = Objects.requireNonNull(publicationFactory, "PublicationFactory is required");
        _iGenreRepo = Objects.requireNonNull(iGenreRepo, "GenreRepo is required");
        _iAuthorRepo = Objects.requireNonNull(iAuthorRepo, "AuthorRepo is required");
    }


    @Transactional
    public Publication registerPublication(Title title, AuthorId authorId, Year releaseYear,
                                           GenreId genreId, String synopsis

    ) {

        if (!_iAuthorRepo.containsOfIdentity(authorId)) {
            throw new NoSuchElementException("Author does not exist");
        }

        if (!_iGenreRepo.containsOfIdentity(genreId)) {
            throw new NoSuchElementException("Genre does not exist");
        }

        Publication newPublication = _publicationFactory.createPublication(title, authorId, releaseYear, genreId, synopsis);

        if (_iPublicationRepo.containsOfIdentity(newPublication.identity())) {
            return _iPublicationRepo.ofIdentity(newPublication.identity())
                    .orElseThrow(() -> new NoSuchElementException("Publication not found"));
        }

        return _iPublicationRepo.save(newPublication);

    }

    @Transactional (readOnly = true)
    public List<Publication> getAllPublications() {

        Iterable<Publication> publications = _iPublicationRepo.findAll();

        List<Publication> response = new ArrayList<>();

        for (Publication publication : publications) {

            response.add(publication);

        }

        return response;
    }

    @Transactional(readOnly = true)
    public Publication getPublicationById(String id) {

        return _iPublicationRepo.ofIdentity(new PublicationId(id))
                .orElseThrow(() -> new NoSuchElementException("Publication with id '" + id + "' does not exist"));
    }


}

