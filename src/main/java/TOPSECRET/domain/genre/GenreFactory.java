package TOPSECRET.domain.genre;

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

}
