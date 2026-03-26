package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

/**
 * Controller responsible for handling the addition of publications to a user's library.
 * <p>
 * This controller interacts with the {@link IItemRepo} and {@link ILibraryRepo}
 * to retrieve available publications and to add selected publications to a user's library.
 * </p>
 */

public class AddPublicationOnLibraryController {
    private final ILibraryRepo _iLibraryRepo;
    private final Library _library;
    private final IItemRepo _iItemRepo;

    public AddPublicationOnLibraryController(ILibraryRepo libraryRepo, Library library, IItemRepo itemRepo) {
        _iLibraryRepo = libraryRepo;
        _library = library;
        _iItemRepo = itemRepo;
    }

    public Library getMyLibrary(User user) {
        return _iLibraryRepo.findLibraryByUser(user);
    }

    public List<Item> getAllItems() {
        return _library.getItemsInLibrary();
    }

    public List<Item> getListOfAvailableItems(User user) {
        Library myLibrary = getMyLibrary(user);
        List<Item> existentItems = myLibrary.getItemsInLibrary();
        return _iItemRepo.getDifferentOf(existentItems);
    }

    public boolean addItemToLibrary(Item selectedItem, User user) {
        Library myLibrary = getMyLibrary(user);
        return myLibrary.addItemToLibrary(selectedItem);
    }
}
