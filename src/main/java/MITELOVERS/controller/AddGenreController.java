package MITELOVERS.controller;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.UserId;

import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling the addition of new genres.
 * <p>
 * This controller is stateless and acts as an intermediary between the user interface
 * and the domain layer, delegating the storage of genres to {@link IGenreRepo}.
 * </p>
 */

@RestController
public class AddGenreController {
    private final IGenreRepo _iGenreRepo;
    private final GenreFactory _genreFactory;

    public AddGenreController(IGenreRepo iGenreRepo, GenreFactory genreFactory, UserId adminId) {

        _iGenreRepo = iGenreRepo;
        _genreFactory = genreFactory;
    }

    public Genre addGenre(String genreName){

        Genre newGenre =  _genreFactory.createGenre(genreName);

        if (_iGenreRepo.containsOfIdentity(newGenre.identity())) {
            throw new IllegalArgumentException("Genre already exists in the repository");
        }
        return _iGenreRepo.save(newGenre);
    }
}

