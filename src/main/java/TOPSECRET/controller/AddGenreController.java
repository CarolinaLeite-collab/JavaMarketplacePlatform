package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreRepo;
import TOPSECRET.domain.User;

/**
 * Controller responsible for handling the addition of new publication genres.
 * <p>
 * This controller is stateless and acts as an intermediary between the user interface
 * and the domain layer, delegating the storage of genres to {@link GenreRepo}.
 * </p>
 */

public class AddGenreController {
    private final GenreRepo _genreRepo;
    private final User _admin;

    public AddGenreController(GenreRepo genreRepo, User admin) {
        _genreRepo = genreRepo;
        _admin = admin;
    }


    public Genre addGenre(String genreName) {
        Genre genre = _genreRepo.addGenre(genreName);
        return genre;
    }
}
