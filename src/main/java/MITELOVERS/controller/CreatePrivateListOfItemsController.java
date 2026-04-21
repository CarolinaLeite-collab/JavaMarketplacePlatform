package MITELOVERS.controller;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.UserId;

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

    public Iterable<Genre> getListOfOfficialGenres() {
        return _iGenreRepo.findAll();
    }

    public ListOfItems createListOfItems(UserId userId, String name, GenreId genreId) {
        return _iListOfItemsRepo.addListOfItems(userId, name, genreId);
    }
}
