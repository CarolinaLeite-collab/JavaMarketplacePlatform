package TOPSECRET.controller;

import TOPSECRET.domain.ILibraryRepo;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.ItemDetails;
import TOPSECRET.domain.User;

import java.util.List;

/**
 * Controller responsible for retrieving the list of items
 * contained in a user's library.
 *
 * <p>This controller interacts with the {@link ILibraryRepo} to obtain
 * the {@link Library} associated with a given {@link User}. It then
 * returns the list of {@link ItemDetails} representing the
 * items stored in that library.</p>
 */

public class ListOfItemsInMyLibraryController {

    private final ILibraryRepo _iLibraryRepo;

    public ListOfItemsInMyLibraryController(ILibraryRepo libraryRepo, User user){
        _iLibraryRepo = libraryRepo;
    }

 public List<ItemDetails> getListOfItemDetails (User user) {
        Library library = _iLibraryRepo.findLibraryByUser(user);

        return library.getItemDetails();
    }
}
