package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the addition of publications (items) to a user's library.
 * <p>
 * This controller coordinates between the {@link IItemRepo} and {@link ILibraryRepo} to:
 * <ul>
 *     <li>Retrieve all available items that are not yet present in any library</li>
 *     <li>Allow a user to add a selected item to their library, if it is not already assigned</li>
 * </ul>
 * </p>
 *
 * <p>
 * It enforces the business rule that an item can only belong to a single library at a time.
 * </p>
 */

public class AddPublicationToLibraryController {
    private final ILibraryRepo _iLibraryRepo;
    private final IItemRepo _iItemRepo;

    public AddPublicationToLibraryController(ILibraryRepo ilibraryRepo, IItemRepo iItemRepo, UserId userId) {
        _iLibraryRepo = ilibraryRepo;
        _iItemRepo = iItemRepo;
    }

    public List<ItemId> getListOfAvailableItemIds(){

        Iterable<ItemId> itemIds = _iItemRepo.findAllKeys();
        Iterable<Library> libraries = _iLibraryRepo.findAll();
        List<ItemId> availableItemIds = new ArrayList<>();


        for (ItemId itemId : itemIds){

            if(!isItemIdAlreadyInAnyLibrary(itemId, libraries)){
                availableItemIds.add(itemId);
            }

        }

        return availableItemIds;

    }

    public boolean addItemIdToLibrary(ItemId selectedItemId, UserId userId){

        Iterable<Library> libraries = _iLibraryRepo.findAll();
        if (isItemIdAlreadyInAnyLibrary(selectedItemId, libraries)) return false;

        LibraryId libraryID = LibraryId.fromUserId(userId);

        Library myLibrary = _iLibraryRepo.ofIdentity(libraryID)
                .orElseThrow(() -> new IllegalStateException("Library not found for user!"));

        return myLibrary.addItemIdToLibrary(selectedItemId);

    }

    private boolean isItemIdAlreadyInAnyLibrary(ItemId itemId, Iterable<Library> libraries) {

        for (Library library : libraries) {
            if (library.getItemsIdInLibrary().contains(itemId)) {
                return true;
            }
        }

        return false;
    }


}
