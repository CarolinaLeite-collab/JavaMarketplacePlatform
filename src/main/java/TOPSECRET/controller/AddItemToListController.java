package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.genre.Genre;

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

    public AddItemToListController(IListOfItemsRepo iListRepo, ILibraryRepo iLibraryRepo, User user) {

        _iListOfItemsRepo = iListRepo;
        _iLibraryRepo = iLibraryRepo;
    }

    public List<ListOfItems> getMyLists(User user) {
        return _iListOfItemsRepo.findListsByUser(user);
    }

    public List<Item> getItemsInMyLibrary(User user) {
        Library lib = _iLibraryRepo.findLibraryByUser(user);
        return lib.getItemsInLibrary();
    }

    public void addItemToList(User user, String listName, Genre genre, Item item) {

        if (listName == null || listName.isBlank()) throw new IllegalArgumentException("List name is mandatory");

        ListOfItems myList = _iListOfItemsRepo.findByOwnerNameAndGenre(user, listName, genre);

        Library lib = _iLibraryRepo.findLibraryByUser(user);

        Item returnedItem = findItemInListOfItems(lib.getItemsInLibrary(), item);

        myList.addItem(returnedItem);
    }

    private Item findItemInListOfItems(List<Item> items, Item item) {

        for (Item i : items) {
            if (i.equals(item)) {
                return i;
            }
        }
        return null;
    }
}