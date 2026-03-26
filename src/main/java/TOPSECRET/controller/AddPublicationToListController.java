package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

/**
 * Controller responsible for handling the addition of new items to a list.
 * <p>
 * This controller interacts with the {@link IListOfPublicationsRepo} and {@link ILibraryRepo}
 * to retrieve available publications and to add selected publications to a user's list.
 * </p>
 */

public class AddPublicationToListController {

    private final IListOfPublicationsRepo _iListOfPublicationsRepo;
    private final ILibraryRepo _iLibraryRepo;

    public AddPublicationToListController(IListOfPublicationsRepo listRepo, ILibraryRepo libraryRepo) {

        _iListOfPublicationsRepo = listRepo;
        _iLibraryRepo = libraryRepo;
    }

    public List<ListOfPublications> getMyLists(User user) {
        return _iListOfPublicationsRepo.findListsByUser(user);
    }

    public List<Item> getItemsInMyLibrary(User user) {
        Library lib = _iLibraryRepo.findLibraryByUser(user); // throws if not found
        return lib.getItemsInLibrary();
    }

    public void addItemToList(User user, String listName, Genre genre, Item item) {

        if (listName == null || listName.isBlank()) throw new IllegalArgumentException("List name is mandatory");

        ListOfPublications myList = _iListOfPublicationsRepo.findByOwnerNameAndGenre(user, listName, genre);

        Library lib = _iLibraryRepo.findLibraryByUser(user);

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