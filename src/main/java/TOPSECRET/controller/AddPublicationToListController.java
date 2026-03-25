package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

/**
 * Controller responsible for handling the addition of new items to a list.
 * <p>
 * This controller interacts with the {@link ListOfPublicationsRepo} and {@link ILibraryRepo}
 * to retrieve available publications and to add selected publications to a user's list.
 * </p>
 */

public class AddPublicationToListController {

    private final ListOfPublicationsRepo _listRepo;
    private final ILibraryRepo _libraryRepo;

    public AddPublicationToListController(ListOfPublicationsRepo listRepo, ILibraryRepo libraryRepo) {

        _listRepo = listRepo;
        _libraryRepo = libraryRepo;
    }

    public List<ListOfPublications> getMyLists(User user) {
        return _listRepo.findListsByUser(user);
    }

    public List<Item> getItemsInMyLibrary(User user) {
        Library lib = _libraryRepo.findLibraryByUser(user); // throws if not found
        return lib.getItemsInLibrary();
    }

    public void addItemToList(User user, String listName, Genre genre, Item item) {

        if (listName == null || listName.isBlank()) throw new IllegalArgumentException("List name is mandatory");

        ListOfPublications myList = _listRepo.findByOwnerNameAndGenre(user, listName, genre);

        Library lib = _libraryRepo.findLibraryByUser(user);

        Item returnedItem = findItemInListOfPublications(lib.getItemsInLibrary(), item);

        myList.addItem(returnedItem);
    }

    private Item findItemInListOfPublications(List<Item> items, Item item) {

        for (Item i : items) {
            if (i.equals(item)) {
                return i;
            }
        }
        return null;
    }
}