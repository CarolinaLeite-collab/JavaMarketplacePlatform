package MITELOVERS.controller;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;

/**
 * Controller responsible for handling the creation of private lists of items for a user.
 */
public class CreatePrivateListOfItemsController {

    private final IListOfItemsRepo _iListOfItemsRepo;
    private final IGenreRepo _iGenreRepo;
    private final ListOfItemsFactory _listOfItemsFactory;

    public CreatePrivateListOfItemsController(
            IListOfItemsRepo iListOfItemsRepo,
            IGenreRepo iGenreRepo,
            ListOfItemsFactory listOfItemsFactory,
            UserId userId) {

        _iListOfItemsRepo = iListOfItemsRepo;
        _iGenreRepo = iGenreRepo;
        _listOfItemsFactory = listOfItemsFactory;
    }

    public Iterable<Genre> getListOfOfficialGenres() {
        return _iGenreRepo.findAll();
    }

    public boolean createListOfItems(UserId userId, String name, GenreId genreId) {
        addListOfItems(userId, name, genreId);
        return true;
    }

    public ListOfItems addListOfItems(UserId userId, String name, GenreId genreId) {
        ListOfItems newList = _listOfItemsFactory.createListOfItems(userId, name, genreId);
        ListOfItemsId id = newList.identity();

        if (_iListOfItemsRepo.containsOfIdentity(id))
            return null;

        _iListOfItemsRepo.save(newList);
        return newList;
    }
}