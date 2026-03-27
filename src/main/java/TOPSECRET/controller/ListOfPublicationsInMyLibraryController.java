package TOPSECRET.controller;

import TOPSECRET.domain.ILibraryRepo;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.PublicationDetails;
import TOPSECRET.domain.User;

import java.util.List;

/**
 * Controller responsible for retrieving the list of publications
 * contained in a user's library.
 *
 * <p>This controller interacts with the {@link ILibraryRepo} to obtain
 * the {@link Library} associated with a given {@link User}. It then
 * returns the list of {@link PublicationDetails} representing the
 * publications stored in that library.</p>
 */

public class ListOfPublicationsInMyLibraryController {
    private final ILibraryRepo _iLibraryRepo;

    public ListOfPublicationsInMyLibraryController(ILibraryRepo libraryRepo, User user){
        _iLibraryRepo = libraryRepo;
    }

 public List<PublicationDetails> getListOfPublicationDetails (User user) {
        Library library = _iLibraryRepo.findLibraryByUser(user);

        return library.getPublicationDetails();
    }
}
