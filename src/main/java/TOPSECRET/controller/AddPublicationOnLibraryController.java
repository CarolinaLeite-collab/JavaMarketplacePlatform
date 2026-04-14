package TOPSECRET.controller;

import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.UserId;

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

    public AddPublicationOnLibraryController(ILibraryRepo ilibraryRepo, Library library, IItemRepo iItemRepo, UserId userId) {
        _iLibraryRepo = ilibraryRepo;
        _library = library;
        _iItemRepo = iItemRepo;
    }

    public Library getMyLibrary(UserId userId) {
        return _iLibraryRepo.findLibraryByUserId(userId);
    }

    public List<ItemId> getAllItems() {
        return _library.getItemsIdInLibrary();
    }

    public List<ItemId> getListOfAvailableItems(UserId userId) {
        Library myLibrary = getMyLibrary(userId);
        List<ItemId> existentItemIds = myLibrary.getItemsIdInLibrary();
        return _iItemRepo.getDifferentOf(existentItemIds);
    }

    public boolean addItemToLibrary(ItemId selectedItemId, UserId userId) {
        Library myLibrary = getMyLibrary(userId);
        return myLibrary.addItemIdToLibrary(selectedItemId);
    }
}
