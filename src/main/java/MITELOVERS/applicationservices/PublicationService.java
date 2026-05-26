package MITELOVERS.services;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class PublicationService {

    private IPublicationRepo _iPublicationRepo;
    private PublicationFactory _publicationFactory;
    private IGenreRepo _iGenreRepo;

    public PublicationService(IPublicationRepo iPublicationRepo,
                              PublicationFactory publicationFactory,
                              IGenreRepo iGenreRepo) {

        _iPublicationRepo = Objects.requireNonNull(iPublicationRepo, "PublicationRepo is required");
        _publicationFactory = Objects.requireNonNull(publicationFactory, "PublicationFactory is required");
        _iGenreRepo = Objects.requireNonNull(iGenreRepo, "GenreRepo is required");
    }


    public Publication registerPublication(Title title,
                                           AuthorId authorId,
                                           Year releaseYear,
                                           GenreId genreId) {

        Publication newPublication = _publicationFactory.createPublication(title, authorId, releaseYear, genreId);

        if (!_iGenreRepo.containsOfIdentity(genreId)) {
            throw new NoSuchElementException(
                    "Genre does not exist in the repository"
            );
        }

        if (_iPublicationRepo.containsOfIdentity(newPublication.identity())){
            throw new IllegalStateException(
                    "Publication already exists in the repository");
        }

        return _iPublicationRepo.save(newPublication);
    }

}

