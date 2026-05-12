package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
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
        return findListsByUserId(userId);
    }

    public List<ItemId> getItemsInMyLibrary(UserId userId) {

        LibraryId libraryId = LibraryId.fromUserId(userId);

        Library lib = _iLibraryRepo.ofIdentity(libraryId)
                .orElseThrow(() -> new IllegalStateException("Library Not Found for user!"));

        return lib.getItemsIdInLibrary();
    }

    public void addItemToList(UserId userId, String listName, GenreId genreId, ItemId itemId) {

        if (listName == null || listName.isBlank()) throw new IllegalArgumentException("List name is mandatory");

        ListOfItems myList = findByOwnerNameAndGenre(userId, listName, genreId);

        if (myList == null) {
            throw new IllegalStateException("List not found");
        }

        myList.addItem(itemId);
    }

    //----------------------------------------------------------------------------------
    // Method to be moved to the future service layer and modified/adapted accordingly
    //----------------------------------------------------------------------------------

    public List<ListOfItems> findListsByUserId(UserId userId) {

        if (userId == null) {
            throw new IllegalArgumentException("UserId is mandatory");
        }

        Iterable<ListOfItems> all = _iListOfItemsRepo.findAll();

        List<ListOfItems> result = new ArrayList<>();
        for (ListOfItems list : all) {
            if (userId.equals(list.getUserId())) {
                result.add(list);
            }
        }

        return result;
    }

    public ListOfItems findByOwnerNameAndGenre(UserId userId, String name, GenreId genreId) {

        if (userId == null) {
            throw new IllegalArgumentException("UserId is mandatory");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("List name is mandatory");
        }
        if (genreId == null) {
            throw new IllegalArgumentException("GenreId is mandatory");
        }

        String normalizedName = name.trim();

        Iterable<ListOfItems> all = _iListOfItemsRepo.findAll();

        for (ListOfItems list : all) {
            boolean sameUser = userId.equals(list.getUserId());
            boolean sameName = list.getName().equalsIgnoreCase(normalizedName);
            boolean sameGenre = genreId.equals(list.getGenreId());

            if (sameUser && sameName && sameGenre) {
                return list;
            }
        }
        return null;
    }

}
