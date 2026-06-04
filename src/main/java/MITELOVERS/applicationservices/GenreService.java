package MITELOVERS.applicationservices;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Service responsible for managing genre-related domain operations.
 */
@Service
public class GenreService {

    private final IGenreRepo _iGenreRepo;
    private final GenreFactory _genreFactory;

    public GenreService(IGenreRepo iGenreRepo, GenreFactory genreFactory) {
        _iGenreRepo = Objects.requireNonNull(iGenreRepo, "GenreRepo is required");
        _genreFactory = Objects.requireNonNull(genreFactory, "GenreFactory is required");
    }

    public Genre registerGenre(String genreName) {
        Genre newGenre = _genreFactory.createGenre(genreName);

        if (_iGenreRepo.containsOfIdentity(newGenre.identity())) {
            throw new IllegalStateException("Genre already exists in the repository");
        }

        return _iGenreRepo.save(newGenre);
    }

    public Iterable<Genre> getAllGenres() {
        return _iGenreRepo.findAll();
    }

    public Optional<Genre> getGenreById(String id) {
        return _iGenreRepo.ofIdentity(new GenreId(id));
    }

    public Iterable<GenreId> getGenresId() {
        return _iGenreRepo.findAllKeys();
    }
}