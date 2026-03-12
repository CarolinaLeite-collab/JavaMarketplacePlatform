package TOPSECRET.controller;

import TOPSECRET.domain.*;

/**
 * Controller responsible for handling the addition of new publication genres.
 * <p>
 * This controller is stateless and acts as an intermediary between the user interface
 * and the domain layer, delegating the storage of genres to {@link GenreRepo}.
 * </p>
 */

public class AddGenreController {
    private final GenreRepo _genreRepo;

    public AddGenreController(GenreRepo genreRepo, User admin) {
        if(!admin.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not allowed to add genres");
        }
        _genreRepo = genreRepo;
    }

    public Genre addGenre(String genreName){
        Genre genre = _genreRepo.addGenre(genreName);

        return genre;
    }
}
