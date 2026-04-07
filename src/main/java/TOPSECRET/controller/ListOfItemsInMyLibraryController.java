package TOPSECRET.controller;

import TOPSECRET.domain.PublicationDetails;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.UserId;

import java.util.List;

/**
 * Controller responsible for retrieving the list of items
 * contained in a user's library.
 *
 * <p>This controller interacts with the {@link ILibraryRepo} to obtain
 * the {@link Library} associated with a given {@link UserId}. It then
 * returns the list of {@link PublicationDetails} representing the
 * items stored in that library.</p>
 */

public class ListOfItemsInMyLibraryController {

    private final ILibraryRepo _iLibraryRepo;

    public ListOfItemsInMyLibraryController(ILibraryRepo libraryRepo, UserId userId){
        _iLibraryRepo = libraryRepo;
    }

 public List<PublicationDetails> getListOfItemDetails (UserId userId) {
        Library library = _iLibraryRepo.findLibraryByUserId(userId);

        return library.getItemDetails();
    }
}
