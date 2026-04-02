package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.GenreId;

/**
 * Factory responsible for creating {@link Genre} instances.
 * <p>
 * @throws IllegalArgumentException if genreName is invalid (as defined by {@link Genre}'s constructor).
 */

public class GenreFactory {

    public Genre createGenre(String genreName) {
        return new Genre(genreName);
    }

    public Genre createGenre(GenreId genreId, String name) {
        return new Genre(genreId, name);
    }
}
