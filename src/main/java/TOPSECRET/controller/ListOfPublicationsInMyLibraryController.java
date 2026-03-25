package TOPSECRET.controller;

import TOPSECRET.domain.*;

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
    private final ILibraryRepo _libraryRepo;

    public ListOfPublicationsInMyLibraryController(ILibraryRepo libraryRepo){
        _libraryRepo = libraryRepo;
    }

 public List<PublicationDetails> getListOfPublicationDetails (User user) {
        Library library = _libraryRepo.findLibraryByUser(user);

        return library.getPublicationDetails();
    }
}
