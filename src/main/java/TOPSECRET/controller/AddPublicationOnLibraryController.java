package TOPSECRET.controller;

import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.UserId;

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

public class AddPublicationOnLibraryController {
    private final ILibraryRepo _iLibraryRepo;
    private final IItemRepo _iItemRepo;

    public AddPublicationOnLibraryController(ILibraryRepo ilibraryRepo, IItemRepo iItemRepo, UserId userId) {
        _iLibraryRepo = ilibraryRepo;
        _iItemRepo = iItemRepo;
    }

    public List<ItemId> getListOfAvailableItemIds(){

        List<ItemId> itemsList = _iItemRepo.findAllKeys();
        List<ItemId> availableItemIds = new ArrayList<>();

        for (ItemId itemId : itemsList){

            if (!_iLibraryRepo.existsItemIdInAnyLibrary(itemId)){
                availableItemIds.add(itemId);
            }

        }

        return availableItemIds;

    }

    public boolean addItemIdToLibrary(ItemId itemId, UserId userId){

        if (_iLibraryRepo.existsItemIdInAnyLibrary(itemId)) return false;

        Library myLibrary = _iLibraryRepo.findLibraryByUserId(userId);

        return myLibrary.addItemIdToLibrary(itemId);

    }


}
