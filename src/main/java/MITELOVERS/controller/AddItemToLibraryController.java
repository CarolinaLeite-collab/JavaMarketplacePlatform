package MITELOVERS.controller;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the addition of items to a user's {@link Library}.
 *
 * It coordinates between the {@link IRepository} for libraries and items to:
 *  - Retrieve all available items not yet assigned to any library
 *  - Add an item to a user's library if it is not already assigned
 *
 * It enforces the business rule that an item can only belong to one library.
 */

@Controller
public class AddItemToLibraryController {

    private final ILibraryRepo _libraryRepo;
    private final IItemRepo _itemRepo;

    public AddItemToLibraryController(ILibraryRepo libraryRepo,
                                      IItemRepo itemRepo) {

        _libraryRepo = libraryRepo;
        _itemRepo = itemRepo;
    }

    public List<ItemId> getListOfAvailableItemIds(){

        Iterable<ItemId> allItemIds = _itemRepo.findAllKeys();
        Iterable<Library> libraries = _libraryRepo.findAll();

        List<ItemId> availableItems = new ArrayList<>();


        for (ItemId itemId : allItemIds){

            if(!isItemIdAlreadyInAnyLibrary(itemId, libraries)){
                availableItems.add(itemId);
            }
        }

        return availableItems;

    }

    public boolean addItemIdToLibrary(ItemId selectedItemId, UserId userId){

        Iterable<Library> libraries = _libraryRepo.findAll();

        if (isItemIdAlreadyInAnyLibrary(selectedItemId, libraries)) {
            return false;
        }

        LibraryId libraryID = LibraryId.fromUserId(userId);

        Library myLibrary = _libraryRepo.ofIdentity(libraryID)
                .orElseThrow(() -> new IllegalStateException("Library not found for user!"));

        boolean added = myLibrary.addItemIdToLibrary(selectedItemId);

        if (added) {
            _libraryRepo.save(myLibrary);
        }

        return added;

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
