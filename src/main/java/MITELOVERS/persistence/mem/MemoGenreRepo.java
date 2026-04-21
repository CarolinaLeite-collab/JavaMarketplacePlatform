package MITELOVERS.persistence.mem;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;

import java.util.*;

/**
 * In-memory implementation of {@link IGenreRepo}.
 * <p>
 * Stores {@link Genre} instances in a {@link HashMap} keyed by {@link GenreId}
 * Prevents duplicate genres based on {@link GenreId} equality
 * </p>
 */

public class MemoGenreRepo implements IGenreRepo {
    private final Map<GenreId, Genre> DATA = new HashMap<>();
    private final GenreFactory _genreFactory;

    public MemoGenreRepo(GenreFactory genreFactory) {
        _genreFactory = genreFactory;
    }

    @Override
    public Genre save(Genre genre) {
        DATA.put(genre.identity(), genre);
        return genre;
    }

    @Override
    public Genre addGenre(String genreName) {

        Genre newGenre =  _genreFactory.createGenre(genreName);

        if (containsOfIdentity(newGenre.identity())) {
            throw new IllegalArgumentException("Genre already exists in the repository");
        }
        return save(newGenre);
    }

    @Override
    public Iterable<Genre> findAll() {
        return DATA.values();
    }

    @Override
    public List<GenreId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());
    }

    @Override
    public Optional<Genre> ofIdentity(GenreId genreId) {
        if (!containsOfIdentity(genreId)) {
            return Optional.empty();
        } else {
            return Optional.of(DATA.get(genreId));
        }
    }

    @Override
    public boolean containsOfIdentity(GenreId genreId) {
        return DATA.containsKey(genreId);
    }
}

