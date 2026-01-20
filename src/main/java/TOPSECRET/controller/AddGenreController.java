package TOPSECRET.controller;

/**
 * Controller responsible for handling the addition of new publication genres.
 * <p>
 * This controller is stateless and acts as an intermediary between the user interface
 * and the domain layer, delegating the creation of genres to the {@link GenreRepo}.
 * </p>
 */

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreRepo;
import TOPSECRET.domain.User;

public class AddGenreController {
    private final GenreRepo _genre;

    public AddGenreController(GenreRepo genre, User admin) {
        _genre = genre;
    }


    public Genre addGenre(String genreName) {
        Genre genre = _genre.create(genreName);
        return genre;
    }
}
