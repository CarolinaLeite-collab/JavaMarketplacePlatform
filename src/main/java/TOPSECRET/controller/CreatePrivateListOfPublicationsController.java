package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;

/**
 * Controller responsible for handling the creation of private lists of publications for a user.
 * <p>
 * This class delegates the actual creation logic to {@link IListOfPublicationsRepo}
 * and providing access to official genres from {@link IGenreRepo}.
 * </p>
 */

public class CreatePrivateListOfPublicationsController {

    private final IListOfPublicationsRepo _iListOfPublicationsRepo;
    private final IGenreRepo _iGenreRepo;

    public CreatePrivateListOfPublicationsController(
            IListOfPublicationsRepo listOfPublicationsRepo,
            IGenreRepo genreRepo) {

        _iListOfPublicationsRepo = listOfPublicationsRepo;
        _iGenreRepo = genreRepo;
    }

    public Iterable<Genre> getListOfOfficialGenres() {
        return _iGenreRepo.findAll();
    }

    public ListOfPublications createListOfPublications(User user, String name, Genre genre) {
        return _iListOfPublicationsRepo.addListOfPublications(user, name, genre);
    }
}