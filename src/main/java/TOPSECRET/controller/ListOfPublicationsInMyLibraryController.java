
package TOPSECRET.controller;

import TOPSECRET.domain.LibraryRepo;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.PublicationDetails;
import TOPSECRET.domain.User;

import java.util.List;


/**
 * Controller responsible for handling the "List publications in my library" use case (US012).
 * Coordinates between UI and domain layer to retrieve publication details.
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
