package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;

import java.util.List;

/**
 * Controller responsible for handling the creation of private lists of items for a user.
 * <p>
 * This class delegates the actual creation logic to {@link IListOfItemsRepo}
 * and providing access to official genres from {@link IGenreRepo}.
 * </p>
 */

public class CreatePrivateListOfItemsController {

    private final IListOfItemsRepo _iListOfItemsRepo;
    private final IGenreRepo _iGenreRepo;

    public CreatePrivateListOfItemsController(
            IListOfItemsRepo iListOfItemsRepo,
            IGenreRepo iGenreRepo,
            User user) {

        _iListOfItemsRepo = iListOfItemsRepo;
        _iGenreRepo = iGenreRepo;
    }

    public Iterable<Genre> getListOfOfficialGenres() {
        return _iGenreRepo.findAll();
    }

    public ListOfItems createListOfItems(User user, String name, Genre genre) {
        return _iListOfItemsRepo.addListOfItems(user, name, genre);
    }
}