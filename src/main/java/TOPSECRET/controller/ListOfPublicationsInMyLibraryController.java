
package TOPSECRET.controller;

import TOPSECRET.domain.LibraryRepo;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.PublicationDetails;
import TOPSECRET.domain.User;

import java.util.List;


/**
 * Controller responsible for listing all publications in a user's library.
 * <p>
 * This controller coordinates with the {@link LibraryRepo} to retrieve a
 * {@link Library} for a given {@link User} and provides detailed information
 * about the publications contained in that library.
 * </p>
 */


public class ListOfPublicationsInMyLibraryController {

    private final LibraryRepo _libraryRepo;


/**
     * Creates a new ListOfPublicationsInMyLibraryController with the specified repository.
     */


     public ListOfPublicationsInMyLibraryController(LibraryRepo libraryRepo, User user){
        _libraryRepo = libraryRepo;
    }


/**
     * Retrieves the list of publications in the user's library.
     *
     * @param user the unique identifier of the user
     * @return list of publications with title, author, type and ISBN/ISSN details
     * @throws IllegalStateException if the user does not have a library
     */

    public List<PublicationDetails> getListOfPublications(User user) {
        Library library = _libraryRepo.findByUser(user);
            return library.getPublicationsInLibrary();
    }

}
