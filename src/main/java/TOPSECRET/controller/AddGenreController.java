package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreRepo;
import TOPSECRET.domain.Role;
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

    public AddGenreController(GenreRepo genreRepo) {
        _genreRepo = genreRepo;
    }

    public Genre addGenre(String genreName, User admin){
        if(!admin.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not allowed to add genres");
        }

        Genre genre = _genreRepo.addGenre(genreName);

        return genre;
    }
}
