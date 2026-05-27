package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.dto.PublicationResponseDTO;
import MITELOVERS.mapper.PublicationResponseDTOMapper;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class PublicationService {

    private IPublicationRepo _iPublicationRepo;
    private PublicationFactory _publicationFactory;
    private IGenreRepo _iGenreRepo;
    private final IAuthorRepo _iAuthorRepo;
    private final PublicationResponseDTOMapper _publicationResponseDTOMapper;

    public PublicationService(IPublicationRepo iPublicationRepo,
                              PublicationFactory publicationFactory,
                              IGenreRepo iGenreRepo,
                              IAuthorRepo iAuthorRepo,
                              PublicationResponseDTOMapper mapper) {

        _iPublicationRepo = Objects.requireNonNull(iPublicationRepo, "PublicationRepo is required");
        _publicationFactory = Objects.requireNonNull(publicationFactory, "PublicationFactory is required");
        _iGenreRepo = Objects.requireNonNull(iGenreRepo, "GenreRepo is required");
        _iAuthorRepo = Objects.requireNonNull(iAuthorRepo, "AuthorRepo is required");
        _publicationResponseDTOMapper = Objects.requireNonNull(mapper, "PublicationDTOAssembler is required");
    }


    public PublicationResponseDTO registerPublication(Title title,
                                                      AuthorId authorId,
                                                      Year releaseYear,
                                                      GenreId genreId
    ) {

        Author author = _iAuthorRepo.ofIdentity(authorId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Author does not exist in the repository"
                ));

        Genre genre = _iGenreRepo.ofIdentity(genreId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Genre does not exist in the repository"
                ));

        Publication newPublication = _publicationFactory.createPublication(title, authorId, releaseYear, genreId);

        if (_iPublicationRepo.containsOfIdentity(newPublication.identity())){
            throw new IllegalStateException(
                    "Publication already exists in the repository");
        }

        Publication savedPublication = _iPublicationRepo.save(newPublication);

        return _publicationResponseDTOMapper.toResponseDTO(
                savedPublication,
                author,
                genre
        );
    }

    public List<PublicationResponseDTO> getAllPublications() {

        Iterable<Publication> publications = _iPublicationRepo.findAll();

        List<PublicationResponseDTO> response = new ArrayList<>();

        for (Publication publication : publications) {

            Author author = _iAuthorRepo.ofIdentity(publication.getAuthorId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Author does not exist in the repository"
                    ));

            Genre genre = _iGenreRepo.ofIdentity(publication.getGenreId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Genre does not exist in the repository"
                    ));

            response.add(
                    _publicationResponseDTOMapper.toResponseDTO(
                            publication,
                            author,
                            genre
                    )
            );
        }

        return response;
    }
}

