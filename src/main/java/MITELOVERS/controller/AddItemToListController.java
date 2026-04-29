package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

public class AddItemToListController {

    private final IListOfItemsRepo _iListOfItemsRepo;
    private final ILibraryRepo _iLibraryRepo;

    public AddItemToListController(IListOfItemsRepo iListRepo,
                                   ILibraryRepo iLibraryRepo,
                                   UserId userId) {
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

    public boolean addItemToList(UserId userId, Name listName, GenreId genreId, ItemId itemId) {
        if (listName == null)
            throw new IllegalArgumentException("List name is mandatory");

        ListOfItems myList = findByOwnerNameAndGenre(userId, listName, genreId);

        if (myList == null)
            throw new IllegalStateException("List not found");

        myList.addItem(itemId);
        _iListOfItemsRepo.save(myList);
        return true;
    }

    public List<ListOfItems> findListsByUserId(UserId userId) {
        if (userId == null)
            throw new IllegalArgumentException("UserId is mandatory");

        Iterable<ListOfItems> all = _iListOfItemsRepo.findAll();
        List<ListOfItems> result = new ArrayList<>();
        for (ListOfItems list : all) {
            if (userId.equals(list.getUserId())) {
                result.add(list);
            }
        }
        return result;
    }

    public ListOfItems findByOwnerNameAndGenre(UserId userId, Name name, GenreId genreId) {
        if (userId == null)
            throw new IllegalArgumentException("UserId is mandatory");
        if (name == null)
            throw new IllegalArgumentException("List name is mandatory");
        if (genreId == null)
            throw new IllegalArgumentException("GenreId is mandatory");

        Iterable<ListOfItems> all = _iListOfItemsRepo.findAll();
        for (ListOfItems list : all) {
            if (userId.equals(list.getUserId())
                    && list.getName().equals(name)
                    && genreId.equals(list.getGenreId())) {
                return list;
            }
        }
        return null;
    }
}