package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

/**
 * Controller responsible for handling the creation of private lists of publications for a user.
 * <p>
 * This class delegates the actual creation logic to {@link ListOfPublicationsRepo}
 * and providing access to official genres from {@link GenreRepo}.
 * </p>
 */
public class CreatePrivateListOfPublicationsController {
    private ListOfPublicationsRepo _listOfPublicationsRepo;
    private GenreRepo _genreRepo;
    private ListOfPublicationsFactory _listOfPublicationsFactory;

    public CreatePrivateListOfPublicationsController(ListOfPublicationsRepo listOfPublicationsRepo, GenreRepo genreRepo, User user) {
        _listOfPublicationsRepo = listOfPublicationsRepo;
        _genreRepo = genreRepo;
        _listOfPublicationsFactory = new ListOfPublicationsFactory();
    }

    public List<Genre> getListOfOfficialGenres() {
        return List.copyOf(_genreRepo.getListOfOfficialGenres());
    }

    public ListOfPublications createListOfPublications(User user, String name, Genre genre) {
        ListOfPublications list = _listOfPublicationsFactory.createListOfPublications(user, name, genre);
        return _listOfPublicationsRepo.addListOfPublications(list);
    }

}