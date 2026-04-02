package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.GenreId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link IGenreRepo}.
 * <p>
 * Stores {@link Genre} instances in a list. Prevents duplicate genres
 * based on {@link Genre#equals(Object)}.
 * </p>
 */

public class MemoGenreRepo implements IGenreRepo {
    private final List<Genre> _genres = new ArrayList<>();
    private final GenreFactory _genreFactory;

    public MemoGenreRepo(GenreFactory genreFactory) {
        _genreFactory = genreFactory;
    }

    @Override
    public Genre save(Genre genre) {
        _genres.add(genre);
        return genre;
    }

    @Override
    public Genre addGenre(String name) {
        GenreId genreId = new GenreId(name);
        if (containsOfIdentity(genreId))
            throw new IllegalArgumentException("Genre already exists in the repository");
        Genre genre = _genreFactory.createGenre(genreId, name);
        return save(genre);
    }

    @Override
    public Iterable<Genre> findAll() {
        return List.copyOf(_genres);
    }

    @Override
    public Optional<Genre> ofIdentity(GenreId genreId) {
        return _genres.stream()
                .filter(g -> g.identity().equals(genreId))
                .findFirst();
    }

    @Override
    public boolean containsOfIdentity(GenreId genreId) {
        return _genres.stream()
                .anyMatch(g -> g.identity().equals(genreId));
    }
}

