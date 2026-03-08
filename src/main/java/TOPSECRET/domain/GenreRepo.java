package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing stored genres of books and magazines.
 * <p>
 * Calls {@link GenreFactory} to generate new genres. Prevents the storage of duplicates based on genre name.
 * </p>
 */

public class GenreRepo {
    private final List<Genre> _genres;
    private final GenreFactory _genreFactory;

    public GenreRepo(GenreFactory genreFactory) {
        _genres = new ArrayList<>();
        _genreFactory = genreFactory;
    }

    public Genre addGenre(String genreName) throws IllegalArgumentException {

        if (genreExists(genreName)) {
            throw new IllegalArgumentException("This genre already exists");
        }

        Genre genre = _genreFactory.createGenre(genreName);
        _genres.add(genre);
        return genre;
    }

    // Checks if a genre with the given name already exists in the repository.
    private boolean genreExists(String genreName) {
        Genre existingGenre = _genreFactory.createGenre(genreName);
        return _genres.contains(existingGenre);
    }

    // Gets the list of official genres, and uses copyOf for encapsulation
    public List<Genre> getListOfOfficialGenres() {

        return List.copyOf(_genres);

    }

}

