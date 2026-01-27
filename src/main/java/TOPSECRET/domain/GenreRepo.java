package TOPSECRET.domain;

/**
 * Repository for managing genres of books and magazines.
 * <p>
 * Allows creating new genres while preventing duplicates based on genre name.
 * </p>
 */

import java.util.ArrayList;
import java.util.List;

public class GenreRepo {
    private final List<Genre> _genres;

    public GenreRepo() {
        _genres = new ArrayList<>();
    }

    public Genre addGenre(String genreName) {

        Genre genre = new Genre(genreName);

        if (existsGenre(genreName)) {
            return null;
        }

        _genres.add(genre);
        return genre;
    }

    // Checks if a genre with the given name already exists in the repository.
    private boolean existsGenre(String genreName) {
        Genre genre = new Genre(genreName);
        boolean exists = _genres.contains(genre);
        return exists;
    }

    // Gets the list of official genres, and uses copyOf for encapsulation
    public List<Genre> getListOfOfficialGenres() {

        List<Genre> listOfOfficialGenres = List.copyOf(_genres);

        return listOfOfficialGenres;

    }

}

