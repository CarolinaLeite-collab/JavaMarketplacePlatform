package TOPSECRET.controller;

import TOPSECRET.domain.user.User;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for handling the addition of new genres.
 * <p>
 * This controller is stateless and acts as an intermediary between the user interface
 * and the domain layer, delegating the storage of genres to {@link IGenreRepo}.
 * </p>
 */

public class AddGenreController {
    private final IGenreRepo _iGenreRepo;

    public AddGenreController(IGenreRepo iGenreRepo, UserId adminId) {

        _iGenreRepo = iGenreRepo;
    }

    public Genre addGenre(String genreName){

        Genre genre = _iGenreRepo.addGenre(genreName);

        return genre;
    }
}
