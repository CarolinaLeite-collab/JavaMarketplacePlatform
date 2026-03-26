package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.IListOfPublicationsRepo;

import java.util.List;

/**
 * Controller responsible for handling the creation of private lists of publications for a user.
 * <p>
 * This class delegates the actual creation logic to {@link IListOfPublicationsRepo}
 * and providing access to official genres from {@link GenreRepo}.
 * </p>
 */

public class CreatePrivateListOfPublicationsController {

    private final IListOfPublicationsRepo _listOfPublicationsRepo;
    private final GenreRepo _genreRepo;

    public CreatePrivateListOfPublicationsController(
            IListOfPublicationsRepo listOfPublicationsRepo,
            GenreRepo genreRepo,
            User user) {

        _listOfPublicationsRepo = listOfPublicationsRepo;
        _genreRepo = genreRepo;
    }

    public List<Genre> getListOfOfficialGenres() {
        return List.copyOf(_genreRepo.getListOfOfficialGenres());
    }

    public ListOfPublications createListOfPublications(User user, String name, Genre genre) {
        return _listOfPublicationsRepo.addListOfPublications(user, name, genre);
    }
}