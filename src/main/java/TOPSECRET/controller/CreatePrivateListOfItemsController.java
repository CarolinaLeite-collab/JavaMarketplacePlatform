package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.IGenreRepo;
import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.ListOfItems.ListOfItems;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.UserId;

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
            UserId userId) {

        _iListOfItemsRepo = iListOfItemsRepo;
        _iGenreRepo = iGenreRepo;
    }

    public List<Genre> getListOfOfficialGenres() {
        return List.copyOf(_iGenreRepo.getListOfOfficialGenres());
    }

    public ListOfItems createListOfItems(UserId userId, String name, GenreId genreId) {
        return _iListOfItemsRepo.addListOfItems(userId, name, genreId);
    }
}
