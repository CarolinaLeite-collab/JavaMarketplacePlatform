package MITELOVERS.persistence.mem;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-memory implementation of {@link IGenreRepo}.
 * <p>
 * Stores {@link Genre} instances in a {@link HashMap} keyed by {@link GenreId}
 * Prevents duplicate genres based on {@link GenreId} equality
 * </p>
 */

@Repository
@Profile("mem")
public class MemGenreRepo implements IGenreRepo {
    private final Map<GenreId, Genre> _data = new HashMap<>();

    @Override
    public Genre save(Genre genre) {
        _data.put(genre.identity(), genre);
        return genre;
    }

    @Override
    public Iterable<Genre> findAll() {
        return _data.values();
    }

    @Override
    public List<GenreId> findAllKeys() {

        return new ArrayList<>(_data.keySet());
    }

    @Override
    public Optional<Genre> ofIdentity(GenreId genreId) {
        if (!containsOfIdentity(genreId)) {
            return Optional.empty();
        } else {
            return Optional.of(_data.get(genreId));
        }
    }

    @Override
    public boolean containsOfIdentity(GenreId genreId) {
        return _data.containsKey(genreId);
    }
}

