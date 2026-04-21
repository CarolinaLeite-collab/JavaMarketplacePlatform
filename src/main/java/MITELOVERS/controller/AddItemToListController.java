package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.UserId;

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

    public List<ItemId> getItemsInMyLibrary(UserId userId) {
        Library lib = _iLibraryRepo.findLibraryByUserId(userId);
        return lib.getItemsIdInLibrary();
    }

    public void addItemToList(UserId userId, String listName, GenreId genreId, ItemId itemId) {

        if (listName == null || listName.isBlank()) throw new IllegalArgumentException("List name is mandatory");

        ListOfItems myList = _iListOfItemsRepo.findByOwnerNameAndGenre(userId, listName, genreId);

        myList.addItem(itemId);
    }
}
