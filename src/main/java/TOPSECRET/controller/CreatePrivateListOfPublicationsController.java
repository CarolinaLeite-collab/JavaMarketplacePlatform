package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

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
            IGenreRepo genreRepo,
            User user) {

        _iListOfPublicationsRepo = listOfPublicationsRepo;
        _iGenreRepo = genreRepo;
    }

    public List<Genre> getListOfOfficialGenres() {
        return List.copyOf(_iGenreRepo.getListOfOfficialGenres());
    }

    public ListOfPublications createListOfPublications(User user, String name, Genre genre) {
        return _iListOfPublicationsRepo.addListOfPublications(user, name, genre);
    }
}