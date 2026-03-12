package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

/**
 * Controller responsible for retrieving the list of publications
 * contained in a user's library.
 *
 * <p>This controller interacts with the {@link LibraryRepo} to obtain
 * the {@link Library} associated with a given {@link User}. It then
 * returns the list of {@link PublicationDetails} representing the
 * publications stored in that library.</p>
 */

public class ListOfPublicationsInMyLibraryController {
    private final LibraryRepo _libraryRepo;
    public ListOfPublicationsInMyLibraryController(LibraryRepo libraryRepo){
        _libraryRepo = libraryRepo;
    }

 public List<Item> getListOfItems(User user) {
        Library library = _libraryRepo.findLibraryByUser(user);
            return library.getItemsInLibrary();
    }
}
