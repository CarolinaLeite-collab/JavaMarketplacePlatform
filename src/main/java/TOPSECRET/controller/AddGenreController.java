package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.IGenreRepo;
import TOPSECRET.domain.Role;
import TOPSECRET.domain.User;

/**
 * Controller responsible for handling the addition of new genres.
 * <p>
 * This controller is stateless and acts as an intermediary between the user interface
 * and the domain layer, delegating the storage of genres to {@link IGenreRepo}.
 * </p>
 */

public class AddGenreController {
    private final IGenreRepo _iGenreRepo;

    public AddGenreController(IGenreRepo iGenreRepo) {

        _iGenreRepo = iGenreRepo;
    }

    public Genre addGenre(User user, String genreName){
        if(!user.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not allowed to add genres");
        }
        Genre genre = _iGenreRepo.addGenre(genreName);

        return genre;
    }
}
