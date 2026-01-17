
package TOPSECRET.controller;

import TOPSECRET.domain.LibraryRepo;
import TOPSECRET.domain.Library;
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


     public ListOfPublicationsInMyLibraryController(LibraryRepo libraryRepo){
        _libraryRepo = libraryRepo;
    }


/**
     * Retrieves the list of publications in the user's library.
     *
     * @param userId the unique identifier of the user
     * @return list of publications with title, author, type and ISBN/ISSN details
     * @throws IllegalStateException if the user does not have a library
     */


    public List<String> getListOfPublications (String userId){
        Library library = _libraryRepo.findByUser(userId);

        if (library == null){
            throw new IllegalStateException("User does not have a library!");
        }

        return library.getPublicationsInLibrary();
    }
}
