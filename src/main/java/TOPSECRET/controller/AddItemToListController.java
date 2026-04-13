package TOPSECRET.controller;

import TOPSECRET.domain.repository.IListOfItemsRepo;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.listofitems.ListOfItems;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.UserId;

import java.util.List;

/**
 * Controller responsible for handling the addition of new items to a list.
 * <p>
 * This controller interacts with the {@link IListOfItemsRepo} and {@link ILibraryRepo}
 * to retrieve available publications and to add selected publications to a user's list.
 * </p>
 */

public class AddItemToListController {

    private final IListOfItemsRepo _iListOfItemsRepo;
    private final ILibraryRepo _iLibraryRepo;

    public AddItemToListController(IListOfItemsRepo iListRepo, ILibraryRepo iLibraryRepo, UserId userId) {
        _iListOfItemsRepo = iListRepo;
        _iLibraryRepo = iLibraryRepo;
    }

    public List<ListOfItems> getMyLists(UserId userId) {
        return _iListOfItemsRepo.findListsByUserId(userId);
    }

    public List<Item> getItemsInMyLibrary(UserId userId) {
        Library lib = _iLibraryRepo.findLibraryByUserId(userId);
        return lib.getItemsInLibrary();
    }

    public void addItemToList(UserId userId, String listName, GenreId genreId, ItemId itemId) {

        if (listName == null || listName.isBlank()) throw new IllegalArgumentException("List name is mandatory");

        ListOfItems myList = _iListOfItemsRepo.findByOwnerNameAndGenre(userId, listName, genreId);

        myList.addItem(itemId);
    }
}
