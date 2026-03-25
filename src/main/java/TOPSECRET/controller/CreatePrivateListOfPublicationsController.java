package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

/**
 * Controller responsible for handling the creation of private lists of publications for a user.
 * <p>
 * This class delegates the actual creation logic to {@link ListOfPublicationsRepo}
 * and providing access to official genres from {@link MemoGenreRepo}.
 * </p>
 */

public class CreatePrivateListOfPublicationsController {

    private final ListOfPublicationsRepo _listOfPublicationsRepo;
    private final IGenreRepo _iGenreRepo;

    public CreatePrivateListOfPublicationsController(
            ListOfPublicationsRepo listOfPublicationsRepo,
            IGenreRepo genreRepo,
            User user) {

        _listOfPublicationsRepo = listOfPublicationsRepo;
        _iGenreRepo = genreRepo;
    }

    public List<Genre> getListOfOfficialGenres() {
        return List.copyOf(_iGenreRepo.getListOfOfficialGenres());
    }

    public ListOfPublications createListOfPublications(User user, String name, Genre genre) {
        return _listOfPublicationsRepo.addListOfPublications(user, name, genre);
    }
}