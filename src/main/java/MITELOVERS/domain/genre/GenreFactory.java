package MITELOVERS.domain.genre;

import MITELOVERS.domain.valueobject.GenreId;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link Genre} instances.
 * <p>
 * @throws IllegalArgumentException if genreName is invalid (as defined by {@link Genre}'s constructor).
 */

@Component
public class GenreFactory {

    public Genre createGenre(String genreName) {
        return new Genre(genreName);
    }

    public Genre createGenre(GenreId genreId, String genreName) {
        return new Genre(genreId, genreName);
    }

}

